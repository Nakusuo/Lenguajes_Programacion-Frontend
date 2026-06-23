package com.minerva.domain.entities.userAction;

import java.time.LocalDateTime;

import com.minerva.domain.constants.Permission;
import com.minerva.domain.valueObject.id.DNI;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.valueObject.id.UserActionIdImpl;

public class UserAction extends Entity<UserActionId> {
    private final UserActionIdImpl userActionIdImpl;
    private final DNI userDNI;
    private final Permission permission;
    private final Id<?> entityId;
    
    private final LocalDateTime registrationDate;

    public UserAction(String userDNI, Permission permission, Entity<?> entity) throws DomainException {
        UserActionIdImpl tempId = UserActionIdImpl.generate();
        super(tempId);
        this.userActionIdImpl = tempId;
        this.userDNI = new DNI(userDNI);

        if (permission == null) throw new DomainException("El permiso no puede ser nulo.");
        this.permission = permission;

        if (entity == null) throw new DomainException("El ID de la entidad no puede ser nulo.");
        this.entityId = entity.getId();

        this.registrationDate = LocalDateTime.now();
    }

    public UserActionIdImpl getUserActionId() {
        return userActionIdImpl;
    }

    public DNI getUserDNI() {
        return userDNI;
    }

    public Permission getPermission() {
        return permission;
    }

    public Id<?> getEntityId() {
        return entityId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}
