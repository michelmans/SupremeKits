package me.alchemi.supremekits.meta;

import me.alchemi.al.objects.meta.BaseMeta;
import me.alchemi.supremekits.main;
import me.alchemi.supremekits.objects.Kit;

public class KitMeta extends BaseMeta {

	
	public KitMeta(Kit currentKit) {
		super(main.getInstance(), currentKit);
	}

}
