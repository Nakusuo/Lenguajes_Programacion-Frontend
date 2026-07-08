package com.minerva.application.service;

import com.minerva.domain.constants.Permission;
import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.result.Result;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.AllId;
import com.minerva.domain.valueObject.id.CustomerName;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnauthorizedActionException;
import com.minerva.domain.repositories.SaleRepository;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.entities.sale.Sale.SaleItem;
import com.minerva.domain.valueObject.id.SaleIdImpl;
import com.minerva.domain.valueObject.id.UserName;
import com.minerva.domain.repositories.CustomerRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class SaleService extends Service {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public SaleService(Role userRole, UserName userName, UserRepository userRepository, SaleRepository saleRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        super(userRole, userName, userRepository);
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // --------------------- WRITE ---------------------


    public Result<Void> registerSale(String customerId, List<Sale.PayData> pays, List<SaleItem> items) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SALE_REGISTER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para registrar ventas.");

        try {
            Sale saleCreated = new Sale(customerId, items);
            if (customerRepository.findById(new CustomerName(customerId)).isEmpty())
                return Result.fail("Cliente no encontrado.");

            Result<Void> addPaysResult = saleCreated.addPays(pays);
            if (addPaysResult.isFail()) return addPaysResult;

            Map<ProductId, ProductQuantity> productQuantities = saleCreated.getProductQuantities();
            Set<Product> products = productRepository.findAllByIds(productQuantities.keySet());

            if (products.size() != productQuantities.size()) return Result.fail("Uno o más productos no encontrados.");

            for (Product product : products) {
                product.processSale(productQuantities.get(product.getId()).value);
            }

            saleRepository.save(saleCreated, products);
            registerUserAction(Permission.SALE_REGISTER, saleCreated.getId());
            return Result.success(null);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    // No tiene sentido que esto reciba como lista el pago, porque una persona si va a pagar se registra como un solo pago general
    public Result<Void> addPaymentToSale(String saleIdStr, List<Sale.PayData> pays) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SALE_ADD_PAYMENT)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para agregar pagos a ventas.");
        SaleIdImpl saleIdImpl;
        try {
            saleIdImpl = SaleIdImpl.fromString(saleIdStr);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
        Optional<Sale> saleOpt = saleRepository.findById(saleIdImpl);
        if (saleOpt.isEmpty()) return Result.fail("Venta no encontrada.");

        Sale sale = saleOpt.get();

        // Debeberia devolver en el result una lista con el id de los pagos que se generaron, para poder registrarlos en el historial de acciones del usuario
        Result<Void> addPaymentResult = sale.addPays(pays);
        if (addPaymentResult.isFail()) return addPaymentResult;

        saleRepository.updatePayments(sale);
        registerUserAction(Permission.SALE_ADD_PAYMENT, sale.getId());
        return Result.success(null);
    }

    // --------------------- READ ---------------------


    public Optional<Sale> findSaleById(String saleId) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SALE_FIND_BY_ID)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar ventas por ID.");
        try {
            return saleRepository.findById(SaleIdImpl.fromString(saleId)).map(sale -> {
                registerUserAction(Permission.SALE_FIND_BY_ID, sale.getId());
                return sale;
            });
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    public List<Sale> findSalesByCustomerId(String customerId) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SALE_FIND_BY_CUSTOMER_ID)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar ventas por ID de cliente.");
        try {
            return saleRepository.findByCustomerId(new CustomerName(customerId))
                    .stream()
                    .peek(sale -> registerUserAction(Permission.SALE_FIND_BY_CUSTOMER_ID, sale.getId()))
                    .toList();
        } catch (DomainException e) {
            return List.of();
        }
    }

    public List<Sale> findAllSales() throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SALE_FIND_ALL)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar todas las ventas.");
        
        List<Sale> sales = saleRepository.findAll();
        registerUserAction(Permission.SALE_FIND_ALL, new AllId());
        return sales;
    }
    
}
