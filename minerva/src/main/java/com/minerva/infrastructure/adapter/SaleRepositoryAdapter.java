package com.minerva.infrastructure.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.minerva.domain.valueObject.id.CustomerId;
import com.minerva.domain.valueObject.id.PayId;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.entities.sale.Sale.PayDTO;
import com.minerva.domain.entities.sale.Sale.SaleDetailDTO;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.valueObject.id.SaleDetailId;
import com.minerva.domain.valueObject.id.SaleId;
import com.minerva.domain.repositories.SaleRepository;
import com.minerva.infrastructure.persistence.entity.CustomerEntity;
import com.minerva.infrastructure.persistence.entity.PayEntity;
import com.minerva.infrastructure.persistence.entity.ProductEntity;
import com.minerva.infrastructure.persistence.entity.SaleDetailEntity;
import com.minerva.infrastructure.persistence.entity.SaleEntity;
import com.minerva.infrastructure.persistence.repository.JpaPayRepository;
import com.minerva.infrastructure.persistence.repository.JpaSaleDetailRepository;
import com.minerva.infrastructure.persistence.repository.JpaSaleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class SaleRepositoryAdapter implements SaleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final JpaSaleRepository saleRepository;
    private final JpaPayRepository payRepository;
    private final JpaSaleDetailRepository saleDetailRepository;

    public SaleRepositoryAdapter(JpaSaleRepository jpaSaleRepository, JpaPayRepository jpaPayRepository, JpaSaleDetailRepository jpaSaleDetailRepository) {
        this.saleRepository = jpaSaleRepository;
        this.payRepository = jpaPayRepository;
        this.saleDetailRepository = jpaSaleDetailRepository;
    }

    @Transactional
    @Override
    public void save(Sale sale) {
        saleRepository.save(toEntity(sale));
        saveSaleDetails(sale.getSaleDetails(), sale.getId());
        savePays(sale.getPays(), sale.getId());
    }

    @Transactional
    public void saveSaleDetails(List<SaleDetailDTO> saleDetails, SaleId saleId) {
        SaleEntity saleEntity = entityManager.getReference(SaleEntity.class, saleId.value);

        for (SaleDetailDTO saleDetailDTO : saleDetails) {
            ProductEntity productEntity = entityManager.getReference(ProductEntity.class, saleDetailDTO.productId());
            
            SaleDetailEntity saleDetailEntity = new SaleDetailEntity(saleDetailDTO.saleDetailId(), saleEntity, productEntity, saleDetailDTO.quantity(), saleDetailDTO.unitPrice());
            saleDetailRepository.save(saleDetailEntity);
        }        
    }

    @Transactional
    public void savePays(List<PayDTO> pays, SaleId saleId) {
        SaleEntity saleEntity = entityManager.getReference(SaleEntity.class, saleId.value);

        for (PayDTO payDTO : pays) {
            PayEntity payEntity = new PayEntity(payDTO.payId(), saleEntity, payDTO.amount(), payDTO.paymentMethod(), payDTO.registrationDate());
            payRepository.save(payEntity);
        }        
    }

    public List<SaleDetailDTO> findSaleDetailBySaleId(SaleId saleId) {
        return saleDetailRepository.findBySaleEntity_SaleId(saleId.value)
                .stream()
                .map(this::toSaleDetailDTO)
                .toList();
    }

    public List<PayDTO> findPayBySaleId(SaleId saleId) {
        return payRepository.findBySaleEntity_SaleId(saleId.value)
                .stream()
                .map(this::toPayDTO)
                .toList();
    }

    @Override
    public Optional<Sale> findById(SaleId saleId) {
        Optional<SaleEntity> saleEntity = saleRepository.findById(saleId.value);
        if (saleEntity.isEmpty()) return Optional.empty();

        List<SaleDetailDTO> saleDetails = findSaleDetailBySaleId(saleId);
        List<PayDTO> pays = findPayBySaleId(saleId);

        return Optional.of(toDomain(saleEntity.get(), saleDetails, pays));
    }

    @Override
    public List<Sale> findByCustomerId(CustomerId customerId) {
        List<SaleEntity> saleEntities = saleRepository.findByCustomerEntity_CustomerNameId(customerId.value);

        return saleEntities.stream()
        .map(saleEntity -> {
            SaleId saleId;

            // OJAZOOO, esto hay que revisar porque no creo que el domain expecion deberia manejarse aqui y/o asi
            // aparte, tengo dudas sobre si deberia lanzar UnexpectedDomainException
            try {
                saleId = SaleId.fromString(saleEntity.getSaleId());
            } catch (DomainException e) {
                throw new UnexpectedDomainException("Error al convertir el ID de venta: " + e.getMessage(), e);
            }

            List<SaleDetailDTO> saleDetails =
                    findSaleDetailBySaleId(saleId);

            List<PayDTO> pays =
                    findPayBySaleId(saleId);

            return toDomain(saleEntity, saleDetails, pays);
        })
        .toList();
    }

    @Override
    public List<Sale> findAll() {     
        List<Sale> sales;

        List<SaleEntity> saleEntities = saleRepository.findAll();
        List<SaleDetailEntity> saleDetails = saleDetailRepository.findAll();
        List<PayEntity> pays = payRepository.findAll();

        sales = new ArrayList<>(saleEntities.size());


        Map<String, List<SaleDetailDTO>> detailsBySaleId = saleDetails.stream().collect(Collectors.groupingBy(
                sd -> sd.getSaleEntity().getSaleId(),
                Collectors.mapping(this::toSaleDetailDTO, Collectors.toList())
            ));

        Map<String, List<PayDTO>> paysBySaleId = pays.stream().collect(Collectors.groupingBy(
                p -> p.getSaleEntity().getSaleId(),
                Collectors.mapping(this::toPayDTO, Collectors.toList())
            ));

        for (SaleEntity saleEntity : saleEntities) {
            List<SaleDetailDTO> detailDTOs = detailsBySaleId.getOrDefault(saleEntity.getSaleId(), List.of());

            List<PayDTO> payDTOs = paysBySaleId.getOrDefault(saleEntity.getSaleId(), List.of());

            sales.add(toDomain(saleEntity, detailDTOs, payDTOs));
        }

        return sales;
    }


    @Override
    public List<SaleDetailDTO> findSaleDetailsById(SaleDetailId id) {
        return saleDetailRepository.findById(id.value)
                .stream()
                .map(this::toSaleDetailDTO)
                .toList();
    }

    @Override
    public List<PayDTO> findPaysById(PayId id) {
        return payRepository.findById(id.value)
                .stream()
                .map(this::toPayDTO)
                .toList();
    }    
 

    private Sale toDomain(SaleEntity entity, List<SaleDetailDTO> saleDetailDTO, List<PayDTO> payDTO) {
        return new Sale(
                entity.getSaleId(),
                entity.getCustomerEntity().getCustomerNameId(),
                entity.getRegistrationDate(),
                saleDetailDTO,
                payDTO
        );
    }

    private SaleEntity toEntity(Sale sale) {
        CustomerEntity customerEntity = entityManager
        .getReference(CustomerEntity.class, sale.getCustomerId());

        return new SaleEntity(
                sale.getId().toString(),
                customerEntity,
                sale.getRegistrationDate()
        );
    }

    private SaleDetailDTO toSaleDetailDTO(SaleDetailEntity entity) {
        return new SaleDetailDTO(
                entity.getSaleDetailId(),
                entity.getProductEntity().getProductNameId(),
                entity.getQuantity(),
                entity.getUnitPrice()
        );
    }

    private PayDTO toPayDTO(PayEntity entity) {
        return new PayDTO(
                entity.getPayId(),
                entity.getAmount(),
                entity.getPaymentMethod(),
                entity.getRegistrationDate()
        );
    }

}
