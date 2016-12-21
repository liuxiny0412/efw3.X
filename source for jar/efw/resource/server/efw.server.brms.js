﻿/**** efw3.X Copyright 2016 efwGrp ****/
/**
 * The class for rule engine calling
 * 
 * @author Chang kejun
 */
function EfwServerBRMS() {
};
/**
 * The function to get a rule by its Id.
 * @param {String} ruleId: required<br>
 * @param {Object} params: required<br>
 *            {param1:value1,param2:value2,...}<br>
 * @param {Date} ruleDate: optional<br>
 * @returns {Record}
 */
EfwServerBRMS.prototype.getRuleById = function(ruleId, params, ruleDate) {
	var values = EfwServerBRMS.prototype._executeQuery({
		"codeType" : "CODETYPE_ID",
		"ruleIndentifier" : ruleId,
		"params" : params,
		"ruleDate" : ruleDate,
	});
	return new Record(values);
};
/**
 * The function to get a rule by its name.
 * @param {String} ruleName: required<br>
 * @param {Object} params: required<br>
 *            {param1:value1,param2:value2,...}<br>
 * @param {Date} ruleDate: optional<br>
 * @returns {Record}
 */
EfwServerBRMS.prototype.getRuleByName = function(ruleName, params, ruleDate) {
	var values = EfwServerBRMS.prototype._executeQuery({
		"codeType" : "CODETYPE_NAME",
		"ruleIndentifier" : ruleName,
		"params" : params,
		"ruleDate" : ruleDate,
	});
	return new Record(values);
};
/**
 * The function to get a rule by its Alias.
 * @param {String} ruleAlias: required<br>
 * @param {Object} params: required<br>
 *            {param1:value1,param2:value2,...}<br>
 * @param {Date} ruleDate: optional<br>
 * @returns {Record}
 */
EfwServerBRMS.prototype.getRuleByAlias = function(ruleAlias, params, ruleDate) {
	var values = EfwServerBRMS.prototype._executeQuery({
		"codeType" : "CODETYPE_ALIAS",
		"ruleIndentifier" : ruleAlias,
		"params" : params,
		"ruleDate" : ruleDate,
	});
	return new Record(values);
};
///////////////////////////////////////////////////////////////////////////////
/**
 * The function to execute rule.
 * 
 * @param executionParams
 *            <br> {<br>
 *            ruleCode:String, //required<br>
 *            params:{param1:value1,param2:value2...}, //required<br>
 *            ruleDate:Date //optional<br> }<br>
 * @returns {Array}
 */
EfwServerBRMS.prototype._executeQuery = function(executionParams) {
	var codeType =executionParams.codeType;
	var ruleIndentifier = executionParams.ruleIndentifier;
	var ruleDate;
	var aryParam = executionParams.params;
	var params = new java.util.HashMap();
	if (executionParams.ruleDate != null) {
		ruleDate = EfwServerFormat.prototype.formatDate(executionParams.ruleDate,"yyyy-MM-dd");
	} else {
		ruleDate = EfwServerFormat.prototype.formatDate(new Date(),"yyyy-MM-dd");
	}
	for ( var key in aryParam) {
		if (key=="debug") continue;// debug function is skipped
		var vl = aryParam[key];

		if (null == vl ||(typeof(vl) == "string" && vl == "")){
			vl = null;
		}
		params.put(key, vl);
	}

	var rs=null;
	if (codeType=="CODETYPE_ID"){rs= Packages.efw.brms.BrmsManager.getRuleById(ruleIndentifier, ruleDate, params);}
	if (codeType=="CODETYPE_NAME"){rs= Packages.efw.brms.BrmsManager.getRuleByName(ruleIndentifier, ruleDate, params);}
	if (codeType=="CODETYPE_ALIAS"){rs= Packages.efw.brms.BrmsManager.getRuleByAlias(ruleIndentifier, ruleDate, params);}
	
	var ret = [];
	var meta = rs.getMetaData();
	var parseValue = function(rs, idx) {
		var value = null;
		if (meta.getColumnType(idx) == com.innoexpert.rulesclient.Constants.TYPE_NUMBER) {
			value = 0 + new Number(rs.getDouble(idx));
		} else if (meta.getColumnType(idx) == com.innoexpert.rulesclient.Constants.TYPE_STRING) {
			value = "" + rs.getString(idx);
		} else if (meta.getColumnType(idx) == com.innoexpert.rulesclient.Constants.TYPE_BOOLEAN) {
			value = true && rs.getBoolean(idx);
		}
		return value;
	};
	// change recordset to java array
	while (rs.next()) {
		var rsdata = {};
		var maxColumnCount = meta.getColumnCount();
		for (var j = 1; j <= maxColumnCount; j++) {
			var key = meta.getColumnName(j);
			rsdata[key] = parseValue(rs, j);
		}
		ret.push(rsdata);
	}
	return ret;
};
