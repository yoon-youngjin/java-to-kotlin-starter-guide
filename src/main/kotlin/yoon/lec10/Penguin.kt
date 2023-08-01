package yoon.lec10

class Penguin(
    species: String,
) : Animal(species, 2), Flyable, Swimable {

    private val wingCount: Int = 2

    override fun move() {
        println("펭귄이 움직입니다~ 꿱꿱")
    }

    override val legCount: Int
        get() = super.legCount + this.wingCount
    override val swimAbility: Int
        get() = 3

    override fun act() {
        super<Swimable>.act()
        super<Flyable>.act()
    }
    override fun fly() {
    }

}