package yoon.lec13

fun main() {
}

class House(
    var address: String,
) {
    var livingRoom = this.LivingRoom(10.0)

    inner class LivingRoom(
        private var area: Double
    ) {
        val address: String
            get() = this@House.address
    }
}

