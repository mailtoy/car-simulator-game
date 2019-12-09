package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.GaugeGUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

public class GaugeTextMaster {
	private static Loader loader;
	private static Map<FontType, List<GaugeGUIText>> texts;
	private static FontRenderer renderer;

	public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
		texts = new HashMap<FontType, List<GaugeGUIText>>();
	}

	public static void render() {
		renderer.renderGauge(texts);
	}

	public static void loadText(GaugeGUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GaugeGUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GaugeGUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}

	public static void removeText(GaugeGUIText text) {
		List<GaugeGUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(texts.get(text.getFont()));
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}
}
