package com.ruoyi.web.util;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.system.domain.TMaterial;

public class MaterialTreeUtil {
	
	private List<TMaterial> list;
	public MaterialTreeUtil(List<TMaterial> list) {
		this.list = list;
	}
	
	public List<TMaterial> buildMaterTree() {
		List<TMaterial> materialList = new ArrayList<>();
		for(TMaterial mater : list) {
			if(mater.getParentId() == -1) {
				mater.setChild(build(mater));
				materialList.add(mater);
			}
		}
		
		return materialList;
	}
	
	private List<TMaterial> build(TMaterial material) {
		List<TMaterial> childMaterialList = getChildren(material);
		if (!childMaterialList.isEmpty()) {
			for (int i = childMaterialList.size() - 1; i >= 0; i--) {
				TMaterial child = childMaterialList.get(i);
				child.setChild(build(child));
			}
		}
		return childMaterialList;
	}
	
	private List<TMaterial> getChildren(TMaterial material) {
		List<TMaterial> childMaterialList = new ArrayList<>();
		for(TMaterial m : list) {
			if(m.getParentId() == material.getId()) {
				childMaterialList.add(m);
			}
		}
		return childMaterialList;
	}
}
