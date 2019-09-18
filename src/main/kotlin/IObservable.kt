interface IObservable {
    val observerSet: MutableSet<IObserver>

    fun registerObserver(newObserver: IObserver)
    fun deregisterObserver(targetObserver: IObserver)

    fun notifyObservers(message: ChatMessage)
}