package com.alchemi.supremekits.meta;

import com.alchemi.al.objects.meta.BaseMeta;
import com.alchemi.supremekits.main;
import com.alchemi.supremekits.objects.Kit;

public class KitMeta extends BaseMeta {

	
	public KitMeta(Kit currentKit) {
		super(main.instance, currentKit);
	}

}
