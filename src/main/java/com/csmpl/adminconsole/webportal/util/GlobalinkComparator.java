package com.csmpl.adminconsole.webportal.util;

import java.util.Comparator;

import com.csmpl.adminconsole.webportal.entity.GlobalLink;

public class GlobalinkComparator implements Comparator<GlobalLink> {

	@Override
	public int compare(GlobalLink gl1, GlobalLink gl2) {
		return gl1.getIntSortNum() - gl2.getIntSortNum();
	}

}
