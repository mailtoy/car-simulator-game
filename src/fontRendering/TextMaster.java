package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import renderEngine.Loader;

public class TextMaster {
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts;
	private static Map<FontType, List<GUIText>> gaugeTexts;
	private static FontRenderer renderer;

	public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
		texts = new HashMap<FontType, List<GUIText>>();
		gaugeTexts = new HashMap<FontType, List<GUIText>>();
	}

	public static void renderText() {
		renderer.render(texts);
	}

	public static void renderGaugeText(String currentSpeed) {
		renderer.renderGauge(gaugeTexts, currentSpeed);
	}

	public static void loadText(GUIText text) {
		addToMap(text, texts);
	}

	public static void loadGaugeText(GUIText text) {
		addToMap(text, gaugeTexts);
	}

	private static void addToMap(GUIText text, Map<FontType, List<GUIText>> map) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = map.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			map.put(font, textBatch);
		}
		textBatch.add(text);
	}

	public static void removeText(GUIText text) {
		remove(text, texts);
	}

	public static void removeGaugeText(GUIText text) {
		remove(text, gaugeTexts);
	}

	private static void remove(GUIText text, Map<FontType, List<GUIText>> map) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			map.remove(map.get(text.getFont()));
		}
	}

	public static void cleanUp() {
		if (texts != null || gaugeTexts != null) {
			renderer.cleanUp();
		}
	}
}