package com.minerva.application.service;

import com.minerva.application.port.drivers.SupplierUseCase;
import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.valueObject.RUC;
import com.minerva.domain.constants.Permission;
import com.minerva.domain.constants.Role;
import com.minerva.domain.entities.result.Result;
import com.minerva.domain.entities.supplier.Supplier;
import com.minerva.domain.valueObject.id.AllId;
import com.minerva.domain.valueObject.id.SupplierName;
import com.minerva.domain.valueObject.id.UserName;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnauthorizedActionException;
import com.minerva.domain.repositories.SupplierRepository;
import com.minerva.domain.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class SupplierService extends Service implements SupplierUseCase {
    private final SupplierRepository supplierRepository;

    public SupplierService(Role userRole, UserName userName, UserRepository userRepository, SupplierRepository supplierRepository) {
        super(userRole, userName, userRepository);
        this.supplierRepository = supplierRepository;
    }

    // --------------------- WRITE ---------------------

    @Override
    public Result<Void> register(String supplierName, String ruc, String phoneNumber) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(com.minerva.domain.constants.Permission.SUPPLIER_REGISTER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para registrar proveedores.");
        
        Supplier supplierCreated;
        try {
            supplierCreated = new Supplier(supplierName, ruc, phoneNumber);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        if (supplierRepository.existsById(supplierCreated.getSupplierName()))
            return Result.fail("Ya existe un proveedor con el mismo nombre.");

        if (supplierCreated.getRuc().isPresent() && supplierRepository.existsByRuc(supplierCreated.getRuc().get()))
            return Result.fail("Ya existe un proveedor con el mismo RUC.");

        if (supplierCreated.getPhoneNumber().isPresent() && supplierRepository.existsByPhoneNumber(supplierCreated.getPhoneNumber().get()))
            return Result.fail("Ya existe un proveedor con el mismo número de teléfono.");

        supplierRepository.save(supplierCreated);
        registerUserAction(Permission.SUPPLIER_REGISTER, supplierCreated.getId());
        return Result.success(null);
    }

    @Override
    public Result<Void> updatePhoneNumber(String supplierName, String phoneNumber) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SUPPLIER_UPDATE_PHONE_NUMBER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para actualizar el número de teléfono del proveedor.");

        Optional<Supplier> supplierOpt = findByPhone(phoneNumber);
        if (supplierOpt.isEmpty()) return Result.fail("Proveedor no encontrado.");

        Supplier supplier = supplierOpt.get();

        Result<Void> updateResult = supplier.updatePhoneNumber(phoneNumber);
        if (updateResult.isFail()) return updateResult;

        if ( supplier.getPhoneNumber().isPresent() && supplierRepository.existsByPhoneNumber(supplier.getPhoneNumber().get()))
            return Result.fail("Ya existe un proveedor con el mismo número de teléfono.");

        supplierRepository.save(supplier);
        registerUserAction(Permission.SUPPLIER_UPDATE_PHONE_NUMBER, supplier.getId());
        return Result.success(null);
    }

    @Override
    public Result<Void> updateRuc(String supplierName, String ruc) throws UnauthorizedActionException {

        if (getUserRole().lacksPermission(Permission.SUPPLIER_UPDATE_RUC)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para actualizar el RUC del proveedor.");

        Optional<Supplier> supplierOpt = findById(supplierName);
        if (supplierOpt.isEmpty()) return Result.fail("Proveedor no encontrado.");

        Supplier supplier = supplierOpt.get();

        Result<Void> updateResult = supplier.updateRuc(ruc);
        if (updateResult.isFail()) return updateResult;

        if (supplier.getRuc().isPresent() && supplierRepository.existsByRuc(supplier.getRuc().get()))
            return Result.fail("Ya existe un proveedor con el mismo RUC.");

        supplierRepository.save(supplier);
        registerUserAction(Permission.SUPPLIER_UPDATE_RUC, supplier.getId());
        return Result.success(null);
    }
    // --------------------- READ ---------------------

    @Override
    public List<Supplier> findAll() throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SUPPLIER_FIND_ALL)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar todos los proveedores.");

        List<Supplier> suppliers = supplierRepository.findAll();
        registerUserAction(Permission.SUPPLIER_FIND_ALL, new AllId());
        return suppliers;
    }

    @Override
    public Optional<Supplier> findById(String supplierName) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SUPPLIER_FIND_BY_ID)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar proveedores por ID.");
        try {
            SupplierName supplierNameObj = new SupplierName(supplierName);
            registerUserAction(Permission.SUPPLIER_FIND_BY_ID, supplierNameObj);
            return supplierRepository.findById(supplierNameObj);
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Supplier> findByRuc(String ruc) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SUPPLIER_FIND_BY_RUC)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar proveedores por RUC.");
        try {
            return supplierRepository.findByRuc(new RUC(ruc)).map(supplier -> {
                registerUserAction(Permission.SUPPLIER_FIND_BY_RUC, supplier.getId());
                return supplier;
            }                
            );
        } catch (DomainException e) {
            return Optional.empty();
        }        
    }

    @Override
    public Optional<Supplier> findByPhone(String phoneNumber) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.SUPPLIER_UPDATE_PHONE_NUMBER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar proveedores por teléfono.");
        try {
            Optional<Supplier> supplierOpt = supplierRepository.findByPhone(new PhoneNumber(phoneNumber));
            supplierOpt.ifPresent(supplier -> registerUserAction(Permission.SUPPLIER_UPDATE_PHONE_NUMBER, supplier.getId()));
            return supplierOpt;
        } catch (DomainException e) {
            return Optional.empty();
        }
    }
}
