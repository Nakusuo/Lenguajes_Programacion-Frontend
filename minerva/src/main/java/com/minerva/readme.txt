Nota:

- los limites de dinero y cantidad de producto deben validarse en infrestrtutra,


- revisar que solo se use BigDecimal en las value keys
- seguir la convencion de id de la db a la hora de nombrar los atributos, osea no es necesario qye una clase se entere que atributo se usa como id de otra


-- cambiar todos los nombres que terminene en obj por el tipo de dato que son sus contraposiciones

-- evitar esar esto en todo el proyecto .compareTo(BigDecimal.ZERO) < 0

-- invertir esta logica             this.phoneNumber = (newPhoneNumber != null)
                ? new PhoneNumber(newPhoneNumber)
                : null;

-- ⚠️ Otra mejora

Aquí:

@Column(name = "ruc", columnDefinition = "CHAR(11)", unique = true)

columnDefinition te acopla al SQL del motor.

Más portable:

@Column(name = "ruc", length = 11, unique = true)

igual para:

phoneNumber
barCode




-- en los dto recibidos falta agregar logica de validacion, pero por factor tiempo se dejo asi nomas

-- deberia agregar un pepper a la encriptación

-- debria agregar un palabra caracteristirca a cada entidad para su id a la hora de registrarse

-- recordar: desaparecer la carpeta id al final, simplemente es para no marearme xd

-- revisar que no se este usando expetion en todo el proyecto, solo se debe usar domain exepction

-- Agregar un campo de estado activo en user
-- ver el tema de vulnerabilidades del pom.xml las pendencias

-- falta agregar el refreshToken

-- Separar los result en dos, uno para mensajes normales y otro para mensajes con codigo

-- El AllId es solo para registrar el caso donde se hacen consultas a muchas entidades

-- No deberia lanzar exepciones el service para el flujo de permisos 
    public enum DomainError {
        UNAUTHORIZED
    }

        // Nota: chat gpt recomienda usar la palbra fuilure en vez de fail, porque dice que failure es sutatntivo
    public class Result<D> {
        private final boolean success;
        private final String message;
        private final D data;
        private final DomainError domainError;

        private Result(boolean success, String message, D data, DomainError domainError) {
            this.success = success;
            this.message = message;
            this.data = data;
            this.domainError = domainError;
        }

        public static <D> Result<D> success(D data) {
            return new Result<>(true, "", data, null);
        }

        public static <D> Result<D> success(D data, String message) {
            return new Result<>(true, message, data, null);
        }

        public static <D> Result<D> fail(String message) {
            return new Result<>(false, message, null, null);
        }

        public static <D> Result<D> fail(DomainError domainError) {
            return new Result<>(false, domainError.name(), null, domainError);
        }

        public boolean isSuccess() { return success; }
        public boolean isFail() {return !success;}
        public String getMessage() { return message; }
        public D getData() { return data; }
        public Optional<DomainError> getDomainError() { return Optional.ofNullable(domainError); }

    }
