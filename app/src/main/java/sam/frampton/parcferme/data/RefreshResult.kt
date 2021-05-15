package sam.frampton.parcferme.data

enum class RefreshResult {
    SUCCESS,
    CACHE,
    NO_RACE_DATA,
    NETWORK_ERROR,
    OTHER_ERROR
}