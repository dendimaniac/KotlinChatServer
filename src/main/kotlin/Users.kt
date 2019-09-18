object Users {
    private val usernameSet: HashSet<String> = hashSetOf()

    fun insertUsername(newUsername: String) {
        usernameSet.add(newUsername)
    }

    fun removeUsername(targetUsername: String) {
        usernameSet.remove(targetUsername)
    }

    fun checkUsernameExist(username: String): Boolean {
        var isExist = false
        usernameSet.forEach {
            isExist = it == username
        }
        return isExist
    }

    override fun toString(): String {
        var allUsername = ""
        for (username in usernameSet) {
            allUsername += "$username\r\n"
        }
        return allUsername
    }
}