package test.sidecar2.components

import org.agmip.translators.annotated.data.DataRange
import spock.lang.Specification

class DataRangeSpec extends Specification {
    private static final String ERR_1 = "[DataRange] data_start_row (value: -1) cannot be less than 1."
    private static final String ERR_2 = "[DataRange] data_end_row (value: 1) cannot be less than or equal to data_start_row (value: 2)."
    private static final String ERR_3 = "[DataRange] data_start_row (value: -1) cannot be less than 1."
    private static final String ERR_4 = "[DataRange] data_end_row (value: -7) cannot be less than or equal to data_start_row (value: -1)."
    def "correct ranges are valid"(Integer start, Integer end, boolean valid) {
        given:
        def range = new DataRange(start, end)
        expect:
        range.isValid() == valid
        where:
        start | end | valid
        1     | 2   | true
        1     | null | true
        -1    | null | false
        2     | 1    | false
        null  | null | true
        1     | 1    | false
    }

    def "right number of reasons are returned"(Integer start, Integer end, int reasons) {
        given: "Invalid data ranges"
        def range = new DataRange(start, end)
        expect:
        range.reasons().size() == reasons
        where:
        start | end  | reasons
        -1    | null | 1
        3     | 1    | 1
        -2    | -7   | 2
        null  | null | 0
        1     | 1    | 1
    }

    def "unbounded end is represented correctly"() {
        given: "end is not provided"
        def range = new DataRange(1, null)
        when: "end() is accessed"
        def end = range.end()
        def str = range.toString()
        then: "-1 or unbounded is provided"
        end == -1
        str.endsWith("unbounded)")
    }

    def "correct reason given when start is less than 0"() {
        given: "start is less than 0"
        def range = new DataRange(-1, null)
        when: "reasons are requested"
        def reasons = range.reasons()
        then: "the correct reason is given"
        reasons.size() == 1
        reasons.get(0) == ERR_1
    }

    def "correct reason given when end is less than start"() {
        given: "start is greater than end"
        def range = new DataRange(2,1)
        when: "reasons are requested"
        def reasons = range.reasons()
        then: "the correct reason is given"
        reasons.size() == 1
        reasons.get(0) == ERR_2
    }

    def "correct reasons given when everything is wrong"() {
        given: "the range is completely wrong"
        def range = new DataRange(-1,-7)
        when: "reasons are requested"
        def reasons = range.reasons()
        then: "the correct reasons are given"
        reasons.size() == 2
        reasons.get(0) == ERR_3
        reasons.get(1) == ERR_4
    }
}
