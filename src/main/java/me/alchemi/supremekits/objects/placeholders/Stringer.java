package me.alchemi.supremekits.objects.placeholders;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.alchemi.al.Library;
import me.alchemi.al.configurations.Messenger;
import me.alchemi.al.objects.base.ConfigBase.IMessage;
import me.alchemi.al.objects.placeholder.IStringer;
import me.alchemi.supremekits.objects.Kit;

public class Stringer implements IStringer {

	private String string;
	
	public Stringer(IMessage message) {
		string = message.value();
	}
	
	public Stringer(String string) {
		this.string = string;
	}
	
	@Override
	public Stringer message(IMessage message) {
		string = message.value();
		return this;
	}

	@Override
	public Stringer player(Player player) {
		string = string.replace("%player%", player.getName());
		return this;
	}

	@Override
	public Stringer player(String player) {
		string = string.replace("%player%", player);
		return this;
	}

	@Override
	public Stringer command(String command) {
		string = string.replace("%command%", command);
		return this;
	}

	@Override
	public Stringer amount(int amount) {
		string = string.replace("%amount%", String.valueOf(amount));
		return this;
	}
	
	public Stringer kit(String kit) {
		string = string.replace("%kit%", kit);
		return this;
	}
	
	public Stringer kit(Kit kit) {
		string = string.replace("%kit%", kit.getDisplayName());
		return this;
	}
	
	public Stringer kitname(Kit kitname) {
		string = string.replace("%kitname%", kitname.getName());
		return this;
	}
	
	public Stringer kitname(String kitname) {
		string = string.replace("%kitname%", kitname);
		return this;
	}

	public Stringer type(String type) {
		string = string.replace("%type%", type);
		return this;
	}
	
	public Stringer x(double x) {
		string = string.replace("%x%", String.valueOf(x));
		return this;
	}
	
	public Stringer y(double y) {
		string = string.replace("%y%", String.valueOf(y));
		return this;
	}
	
	public Stringer z(double z) {
		string = string.replace("%z%", String.valueOf(z));
		return this;
	}
	
	@Override
	public Stringer parse(Player player) {
		string = Library.getParser().parse(player, string);
		return this;
	}

	@Override
	public Stringer parse(OfflinePlayer player) {
		string = Library.getParser().parse(player, string);
		return this;
	}

	@Override
	public Stringer parse(CommandSender sender) {
		string = Library.getParser().parse(sender, string);
		return this;
	}

	@Override
	public String create() {
		return Messenger.formatString(string);
	}

}
