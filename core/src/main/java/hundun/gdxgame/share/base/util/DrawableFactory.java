package hundun.gdxgame.share.base.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author hundun
 * Created on 2022/08/30
 */
public class DrawableFactory {
    public static Drawable createBorderBoard(int width, int height, float grayColor, int borderWidth) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGB565);
        pixmap.setColor(grayColor + 0.1f, grayColor + 0.1f, grayColor + 0.1f, 1.0f);
        pixmap.fill();
        pixmap.setColor(grayColor, grayColor, grayColor, 1.0f);
        pixmap.fillRectangle(borderWidth, borderWidth, width - borderWidth * 2, height - borderWidth * 2);
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        return drawable;
    }

    public static Drawable getSimpleBoardBackground() {
        return createBorderBoard(10, 10, 0.8f, 1);
    }
}
