Nota:

- los limites de dinero y cantidad de producto deben validarse en infrestrtutra,


- revisar que solo se use BigDecimal en las value keys
- seguir la convencion de id de la db a la hora de nombrar los atributos, osea no es necesario qye una clase se entere que atributo se usa como id de otra

. Nombre confuso: customerNameId
String customerNameId

Eso mezcla dos conceptos:

name
id

Debe ser:

String customerId

-- cambiar todos los nombres que terminene en obj por el tipo de dato que son sus contraposiciones

-- evitar esar esto en todo el proyecto .compareTo(BigDecimal.ZERO) < 0