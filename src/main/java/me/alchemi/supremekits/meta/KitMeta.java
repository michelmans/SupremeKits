package me.alchemi.supremekits.meta;

import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.supremekits.Supreme;
import me.alchemi.supremekits.objects.Kit;

public class KitMeta extends BaseMeta {

	
	public KitMeta(Kit currentKit) {
		super(Supreme.getInstance(), currentKit);
	}

}
