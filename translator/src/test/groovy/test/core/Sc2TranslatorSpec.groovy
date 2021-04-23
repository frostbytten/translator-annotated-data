package test.core

import org.agmip.translators.annotated.Sc2Translator
import spock.lang.Specification

class Sc2TranslatorSpec extends Specification {
    def "files are added"() {
        given: "A new Sc2Translator"
        def tr = new Sc2Translator()
        when: "a new file is added"
        tr.addFile("somefile.txt")
        def res = tr.getInputFilenames()
        then: "the file is in the repository"
        res.size() == 1
        res.contains("somefile.txt")
    }
}
