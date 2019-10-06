import kotlinx.serialization.Serializable

@Serializable
class ChatMessage(val username: String, val command: Commands, val message: String, val timestamp: String) {
    fun asString(): String {
        return "$username: $message, $timestamp"
    }
}