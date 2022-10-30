package com.ll.exam.sb_batch_exam;

import com.ll.exam.sb_batch_exam.util.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTests {
    @Test
    @DisplayName("특정월에 마지막날을 계산")
    void t1() {
        int endDayOf__2022_09 = Util.date.getEndDayOf(2022, 9);

        assertThat(endDayOf__2022_09).isEqualTo(30);

        int endDayOf__2022_08 = Util.date.getEndDayOf(2022, 8);

        assertThat(endDayOf__2022_08).isEqualTo(31);
    }
}
