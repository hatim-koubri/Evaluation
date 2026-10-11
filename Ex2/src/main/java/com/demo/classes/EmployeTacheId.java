package com.demo.classes;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeTacheId implements Serializable {
    @Column(name = "employe_id") private int employeId;
    @Column(name = "tache_id") private int tacheId;
    public EmployeTacheId() { }
    public EmployeTacheId(int employeId, int tacheId) { this.employeId = employeId; this.tacheId = tacheId; }
    public int getEmployeId() { return employeId; }
    public void setEmployeId(int employeId) { this.employeId = employeId; }
    public int getTacheId() { return tacheId; }
    public void setTacheId(int tacheId) { this.tacheId = tacheId; }
    @Override public boolean equals(Object autre) {
        if (this == autre) return true;
        if (!(autre instanceof EmployeTacheId)) return false;
        EmployeTacheId that = (EmployeTacheId) autre;
        return employeId == that.employeId && tacheId == that.tacheId;
    }
    @Override public int hashCode() { return Objects.hash(employeId, tacheId); }
}