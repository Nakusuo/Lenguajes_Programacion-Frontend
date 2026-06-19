package com.minerva.domain.entities.userAction;

import java.time.LocalDateTime;

import com.minerva.domain.constants.Permission;
import com.minerva.domain.valueObject.id.DNI;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.valueObject.id.UserActionId;

public class UserAction extends Entity {
    private final UserActionId userActionId;
    private final DNI userDNI;
    private final Permission permission;
    private final String entityId;
    
    private final LocalDateTime registrationDate;

    public UserAction(String userDNI, Permission permission, Id entityId) throws DomainException {
        UserActionId tempId = UserActionId.generate();
        super(tempId);
        this.userActionId = tempId;
        this.userDNI = new DNI(userDNI);

        if (permission == null) throw new DomainException("El permiso no puede ser nulo.");
        this.permission = permission;

        if (entityId == null) throw new DomainException("El ID de la entidad no puede ser nulo.");
        this.entityId = entityId.value();
        
        this.registrationDate = LocalDateTime.now();
    }


}
