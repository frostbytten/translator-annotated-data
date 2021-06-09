package org.agmip.translators.annotated.sidecar2;

public class Sidecar2Keys {
  // Mapping Info Strings
  public static final String MI_FIELD = "mapping_info";
  public static final String MIMA_FIELD = "mapping_author";
  public static final String MISU_FIELD = "source_url";
  public static final String MIVV_FIELD = "vmapper_version";
  // Dataset Metadata Strings
  public static final String DM_FIELD = "dataset_metadata";
  // AgMIP Translation Mapping Strings
  public static final String ATM_FIELD = "agmip_translation_mappings";
  public static final String COL_FIELD = "column_index";
  public static final String TABLE_FIELD = "table_index";
  // -- Relations
  public static final String REL_FIELD = "relations";
  public static final String RELF_FIELD = "from";
  public static final String RELT_FIELD = "to";
  public static final String REL_F_FIELD = "file";
  public static final String REL_S_FIELD = "sheet";
  public static final String REL_K_FIELD = "keys";
  public static final String REL_KIF_FIELD = "ignored_flag";
  // -- Files
  public static final String FILES_FIELD = "files";
  public static final String FILE_FIELD = "file";
  // ---- File Metadata
  public static final String FM_FIELD = "file_metadata";
  public static final String FMN_FIELD = "file_name";
  public static final String FMC_FIELD = "content-type";
  public static final String FMU_FIELD = "file_url";
  // ---- Sheets
  public static final String S_FIELD = "sheets";
  public static final String SN_FIELD = "sheet_name";
  public static final String SI_FIELD = "sheet_index";
  public static final String SHR_FIELD = "header_row";
  public static final String SDSR_FIELD = "data_start_row";
  public static final String SDER_FIELD = "data_end_row";
  // ------ Mappings
  public static final String SM_FIELD = "mappings";
  public static final String SMC_FIELD = "category";
  public static final String SMCI_FIELD = "column_index";
  public static final String SMCD_FIELD = "code_descriptions";
  public static final String SMF_FIELD = "format";
  public static final String SMI_FIELD = "icasa";
  public static final String SMU_FIELD = "unit";
  public static final String SMV_FIELD = "value";
  // -------- Formulas
  public static final String FORM_FIELD = "formula";
  public static final String FORMF_FIELD = "function";
  public static final String FORMA_FIELD = "args";
  // ---------- join_column_function fields
  public static final String FUN_JC_VVK_FIELD = "virtual_val_keys";
  public static final String FUN_JC_VD_FIELD = "virtual_divider";
  public static final String FUN_JC_VINF_FIELD = "virtual_ignore_null_flg";
  // -- xrefs
  public static final String XREF_FIELD = "xrefs";
  // -- Supported Content-Types
  public static final String MIME_CSV = "text/csv";
  public static final String MIME_XLSX =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static final String EXT_CSV = ".csv";
  public static final String EXT_XLSX = ".xlsx";
  public static final String[] SUPPORTED_MIME = {MIME_CSV, MIME_XLSX};
  public static final String[] SUPPORTED_EXT = {EXT_CSV, EXT_XLSX};
}
