package ch1;

import org.springframework.stereotype.Component;

/**
 * Created by lszhen on 2017/3/24.
 */
@Component
public class SgtPeppers implements CompactDisc {

    private String title = "SgtPepper.tittle";
    private String artist = "The Beatles";
    @Override
    public void play() {
        System.out.println("Playing "+title+" by "+artist);
    }
}
