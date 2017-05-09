package sia.knights;

import static org.mockito.Mockito.*;

import org.junit.Test;

import sia.knights.BraveKnight;
import sia.knights.Quest;

/**
 * Name: admin
 * Date: 2017/5/9
 * Time: 16:00
 */
public class BraveKnightTest {

    @Test
    public void knightShouldEmbarkOnQuest() {
        Quest mockQuest = mock(Quest.class);
        BraveKnight knight = new BraveKnight(mockQuest);
        knight.embarkOnQuest();
        verify(mockQuest, times(1)).embark();//Mockito框架验证Quest的mock实现的embark()方法仅仅被调用了一次
    }
}