package beans

import groovy.transform.Canonical

@Canonical
class Variable {

    Float value = 0

    Variable() {
    }

    Variable(Float value) {
        this.value = value
    }
}
