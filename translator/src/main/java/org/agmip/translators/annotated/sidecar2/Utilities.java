package org.agmip.translators.annotated.sidecar2;

import java.util.Comparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public enum Utilities {
	INSTANCE;
	private final static Comparator<Sc2Rule> colCompare = Comparator.comparing(Sc2Rule::getColumnIndex);
	public final static ObjectMapper mapper = new ObjectMapper();
	public static Integer convertStringToInteger(String val) {
		Integer retval = null;
		try {
			retval = Integer.valueOf(val);
		} catch (NumberFormatException _ex) {
			// Log the error
		}
		return retval;
	}
	public static int getMaxSheetColumn(Sc2Sheet sheet) {
		int maxval = 0;
		Sc2Rule maxRule = sheet.rules().stream().max(colCompare).get();
		return maxRule.getColumnIndex();
	}
}
