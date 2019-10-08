object Users {
    private val usernameSet: HashSet<String> = hashSetOf()

    init {
        usernameSet.clear()
    }

    fun insertUsername(newUsername: String) {
        usernameSet.add(newUsername)
        println("Username $newUsername added!")
    }

    fun removeUsername(targetUsername: String) {
        usernameSet.remove(targetUsername)
        println("Username $targetUsername removed!")
    }

    fun checkUsernameExist(username: String): Boolean {
        return usernameSet.contains(username)
    }

    override fun toString(): String {
        var allUsername = ""
        for (username in usernameSet) {
            allUsername += "$username\r\n"
        }
        return allUsername
    }
}