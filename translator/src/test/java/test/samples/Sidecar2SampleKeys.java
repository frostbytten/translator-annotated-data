package test.samples;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.*;
import static org.agmip.translators.annotated.sidecar2.Utilities.mapper;
import static org.agmip.translators.annotated.sidecar2.functions.Sc2Example.EXAMPLE_FUN_ARG;

import com.fasterxml.jackson.databind.JsonNode;

public class Sidecar2SampleKeys {
  public static final String MIMA_VAL = "test@example.com";
  public static final String MISU_VAL = "https://example.com/";
  public static final String MIVV_VAL = "1.5.0";
  public static final String FMFN_CSV_VAL = "example.csv";
  public static final String FMCT_CSV_VAL = "text/csv";
  public static final String FMFN_XLSX_VAL = "example.xlsx";
  public static final String FMFU_XLSX_VAL = "https://example.com/example.xlsx";
  public static final String FMCT_XLSX_VAL =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static final String FMFN_UNS_VAL = "invalid.file";
  public static final String FMCT_UNS_VAL = "invalid/content-type";
  public static final String FMFU_UNS_VAL = "invalid.url";
  public static final String SSN_VAL = "Sheet1";
  public static final int SDSR_VAL = 2;
  public static final int SDER_VAL = 10;
  public static final int SD_R_INVAL = -7;
  public static final int SDER_INVAL = 1;
  public static final String SMI_VAL = "exname";
  public static final String SMU_VAL = "m^2";
  public static final int SMCI_VAL = 2;
  public static final int SMC_VAL = 1234;
  public static final int SMCI_INVAL = -7;
  public static final int SMC_INVAL = -7;
  public static final int SM_INT_INVALID = -1;
  public static final String SMV_VAL = "123";
  public static final String SMF_VAL = "YYYY-mm-dd";
  public static final String FORMF_VAL = "example_fun";
  public static final String FORMF_INVAL = "bad_function";
  public static final String FORMA_ANS = "echo echo echo";
  public static final String FORMA_FINAL_ANS = "I should repeat: " + FORMA_ANS;
  public static final JsonNode FORMA_VAL =
      mapper.createObjectNode().put(EXAMPLE_FUN_ARG, FORMA_ANS);
}
