package org.agmip.translators.annotated.sidecar2.components;

import io.vavr.collection.List;
import io.vavr.collection.Seq;

public class Sc2Relation {
  private final Sc2RelationKey _primary;
  private final Sc2RelationKey _foreign;
  private boolean valid;

  public Sc2Relation(Sc2RelationKey primary, Sc2RelationKey foreign) {
    this._primary = primary;
    this._foreign = foreign;
  }

  public Sc2RelationKey getPrimary() {
    return _primary;
  }

  public Sc2RelationKey getForeign() {
    return _foreign;
  }

  public Seq<Sc2RelationKey> getKeys() {
    return List.of(_primary, _foreign);
  }
}
