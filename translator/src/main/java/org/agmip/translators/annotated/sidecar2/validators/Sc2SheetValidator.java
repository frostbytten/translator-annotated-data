package org.agmip.translators.annotated.sidecar2.validators;

import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.MIME_XLSX;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SDER_FIELD;
import static org.agmip.translators.annotated.sidecar2.Sidecar2Keys.SDSR_FIELD;
import static org.agmip.translators.annotated.sidecar2.Utilities.tryStringToInteger;

import java.util.Objects;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.agmip.translators.annotated.sidecar2.components.Sc2Rule;
import org.agmip.translators.annotated.sidecar2.components.Sc2Sheet;

public class Sc2SheetValidator {
  public Validation<Seq<String>, Sc2Sheet> validate(
      String name,
      String dataStartRow,
      String dataEndRow,
      List<Validation<Seq<String>, Sc2Rule>> rules,
      String fileType) {
    Validation<String, Integer> dsr = tryStringToInteger(dataStartRow, SDSR_FIELD);
    Validation<String, Integer> der = tryStringToInteger(dataEndRow, SDER_FIELD);
    return Validation.combine(
            validateName(name, fileType), dsr, validateOrder(dsr, der), Validation.valid(rules))
        .ap(Sc2Sheet::new);
  }

  private Validation<String, String> validateName(String name, String fileType) {
    return (fileType.equals(MIME_XLSX) && Objects.isNull(name))
        ? Validation.invalid("XLSX files must provide sheet names.")
        : Validation.valid(name);
  }

  private Validation<String, Integer> validateOrder(
      Validation<String, Integer> dsr, Validation<String, Integer> der) {
    if (dsr.isInvalid() || der.isInvalid()) return der;
    int _dsr = dsr.get();
    int _der = der.get();
    if (_der == -1) {
      return _der < _dsr
          ? Validation.invalid(
              "data_end_row must(" + _der + " be after data_start_row" + _dsr + ".")
          : der;
    } else {
      return der;
    }
  }
}
