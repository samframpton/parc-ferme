package sam.frampton.parcferme.api.dtos

data class ConstructorDto(
    val constructorId: String,
    val name: String,
    val nationality: String,
    val url: String?
)