package beans

import groovy.transform.Canonical

@Canonical
class Variable {

    Integer value = 0

    Variable() {
    }

    Variable(Integer value) {
        this.value = value
    }
}
