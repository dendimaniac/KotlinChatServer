import kotlinx.serialization.Serializable

@Serializable
class ChatMessage(val message: String, val userName: String, val timestamp: String) {
    override fun toString(): String {
        return "$timestamp, $userName: $message"
    }
}