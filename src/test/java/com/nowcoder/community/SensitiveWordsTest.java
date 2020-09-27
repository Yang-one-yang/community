package com.nowcoder.community;

import com.nowcoder.community.utils.SensitiveWordsFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveWordsTest {
    @Autowired
    private SensitiveWordsFilter sensitiveWordsFilter;

    @Test
    public void testSensitiveWordsFilter() {
        String text = "这里可以赌博，嫖娼，吸毒，开票，哈哈哈！";
        String filter = sensitiveWordsFilter.filter(text);
        System.out.println(filter);
    }
}
