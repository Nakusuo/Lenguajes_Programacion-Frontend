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