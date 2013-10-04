import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

public class EmojiUtil {
	HashMap<String, String> unicode;
	Pattern p;
	private final int CarrierID = 0; // 0:Docomo, 1:KDDI. 2: Softbank

	// Emoji data
	/**
	 * Docomo Hex: E63E-E73D Dec: 58942-59197
	 * 
	 * AU Hex: E488-EB7B Dec: 58504-60283
	 * 
	 * Softbank Hex: E04A-E22A Dec: 57418-57898
	 */

	private EmojiUtil() {
		p = Pattern.compile("[&][#][\\w]{4,}[;]");
	}

	private static EmojiUtil instance = null;

	public static EmojiUtil getInstance() {
		if (instance == null) {
			instance = new EmojiUtil();

		}
		return instance;
	}

	public String DecToHexString(int dec) {
		return Integer.toHexString(dec).toUpperCase(Locale.JAPANESE);
	}

	@SuppressLint("DefaultLocale")
	public String ConvertFromBinary(String html) {
		CharBuffer ss = CharBuffer.wrap(html);
		StringBuffer ostr = new StringBuffer();

		for (int i = 0; i < ss.length(); i++) {

			if ((ss.get(i) >= 0x0020) && (ss.get(i) <= 0x007e)) {

				ostr.append(ss.get(i)); // No.
			} else // Yes.
			{
				String hex = Integer.toHexString(ss.get(i) & 0xFFFF).toLowerCase(); // Get

				if (hex != "a") {
					String tmp = "";
					switch (CarrierID) {
					case 0:
						tmp = DocomoEmoji.unicode.get(hex);
						break;
					case 1:
						tmp = KddiEmoji.unicode.get(DecToHexString(Integer.parseInt(hex)));
						break;
					case 2:
						tmp = SoftbankEmoji.unicode.get(DecToHexString(Integer.parseInt(hex)));
						break;
					default:
						break;
					}

					if (tmp != null) {

						ostr.append(tmp);
					} else {
						ostr.append(ss.get(i));

					}
				}
			}

		}

		return (new String(ostr));

	}

}
