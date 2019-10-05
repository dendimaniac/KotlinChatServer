import kotlinx.serialization.Serializable

@Serializable
class ChatMessage(val message: String, val username: String, private val timestamp: String) {
    override fun toString(): String {
        return "$timestamp, $username: $message"
    }
}