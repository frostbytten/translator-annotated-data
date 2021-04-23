package org.agmip.translators.annotated.sidecar2.components;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.MIME_XLSX;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sc2Sheet {
  private static final Logger LOG = LoggerFactory.getLogger(Sc2Sheet.class);

  private final String _context;
  private final String _name;
  private final int _dsr;
  private final int _der;
  private final List<Sc2Rule> _rules;
  private boolean valid;

	public Sc2Sheet(String name, Integer dsr, Integer der, List<Sc2Rule> rules, String context) {
		this._name = name;
		this._dsr = dsr == null ? 0 : dsr;
		this._der = der == null ? -1 : der;
		this._context = context;
		this._rules = rules;
		this.valid = validate();
	}

  public Optional<String> getName() {
    return Optional.ofNullable(_name);
  }

  public String tryName(String orElse) {
    return _name == null ? orElse : _name;
  }

  public int getDataStartRow() {
    return _dsr;
  }

  public int getDataEndRow() {
    return _der;
  }

  public List<Sc2Rule> rules() {
    return _rules;
  }

  public boolean isValid() {
    return valid;
  }

  public void invalidate() {
    valid = false;
  }

  private boolean validate() {
    boolean validated = true;
    if (_context.equals(MIME_XLSX) && (_name == null)) {
      LOG.error("XLSX file must provide a sheet name for translation");
      validated = false;
    }
    if ((_der != -1) && (_der <= _dsr)) {
      LOG.error("data_end_row must be after data_start_row");
      validated = false;
    }
    if ((_dsr < -1)) {
      LOG.error("data_start_row is not valid [" + _dsr + "]");
      validated = false;
    }
    if ((_der < -1)) {
      LOG.error("data_end_row is not valid [" + _der + "]");
      validated = false;
    }
    return validated;
  }
}
