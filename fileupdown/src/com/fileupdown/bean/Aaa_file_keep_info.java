package com.fileupdown.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lys.sys.utils.annotation.MyTable;


@Entity
@Table(name = "Aaa_file_keep_info")
@MyTable(tableName = "Aaa_file_keep_info",pkName="fki_nm_id")
public class Aaa_file_keep_info implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7506796525109120183L;
	@Id
	@Column(length = 20)private Integer fki_nm_id;
	@Column private Date fki_tt_cd;
	@Column private Date fki_tt_md;
	@Column(length = 200)private String fki_st_fn;
	@Column(length = 20)private Integer fki_st_fz;
	@Column(length = 200)private String fki_st_fp;
	@Column(length = 50)private String fki_st_ft;
	@Column(length = 20)private String fki_st_ip;
	public Integer getFki_nm_id() {
		return fki_nm_id;
	}
	public void setFki_nm_id(Integer fki_nm_id) {
		this.fki_nm_id = fki_nm_id;
	}
	public Date getFki_tt_cd() {
		return fki_tt_cd;
	}
	public void setFki_tt_cd(Date fki_tt_cd) {
		this.fki_tt_cd = fki_tt_cd;
	}
	public Date getFki_tt_md() {
		return fki_tt_md;
	}
	public void setFki_tt_md(Date fki_tt_md) {
		this.fki_tt_md = fki_tt_md;
	}
	public String getFki_st_fn() {
		return fki_st_fn;
	}
	public void setFki_st_fn(String fki_st_fn) {
		this.fki_st_fn = fki_st_fn;
	}
	public Integer getFki_st_fz() {
		return fki_st_fz;
	}
	public void setFki_st_fz(Integer fki_st_fz) {
		this.fki_st_fz = fki_st_fz;
	}
	public String getFki_st_fp() {
		return fki_st_fp;
	}
	public void setFki_st_fp(String fki_st_fp) {
		this.fki_st_fp = fki_st_fp;
	}
	public String getFki_st_ft() {
		return fki_st_ft;
	}
	public void setFki_st_ft(String fki_st_ft) {
		this.fki_st_ft = fki_st_ft;
	}
	public String getFki_st_ip() {
		return fki_st_ip;
	}
	public void setFki_st_ip(String fki_st_ip) {
		this.fki_st_ip = fki_st_ip;
	}
	
}
