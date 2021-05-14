package org.agmip.translators.annotated.sidecar2.components;

import java.util.Optional;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sc2Sheet {
  private static final Logger LOG = LoggerFactory.getLogger(Sc2Sheet.class);

  private final String _name;
  private final int _dsr;
  private final int _der;
  private final List<Validation<Seq<String>, Sc2Rule>> _rules;
  public ComponentState _state;

  public Sc2Sheet(
      String name, Integer dsr, Integer der, List<Validation<Seq<String>, Sc2Rule>> rules) {
    this._name = name;
    this._dsr = dsr == null ? 0 : dsr;
    this._der = der == null ? -1 : der;
    this._rules = rules;
    _state = setState();
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
    return _rules.filter(Validation::isValid).map(Validation::get);
  }

  public List<Validation<Seq<String>, Sc2Rule>> rawRules() {
    return _rules;
  }

  public ComponentState rulesState() {
    return _state;
  }

  private ComponentState setState() {
    if (_rules.forAll(Validation::isValid)) return ComponentState.COMPLETE;
    if (_rules.find(Validation::isValid).isDefined()) return ComponentState.PARTIAL;
    return ComponentState.INVALID;
  }
}
