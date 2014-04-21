package com.liubing.common.memcached.client.command;

import com.liubing.common.memcached.reponse.SetReponse;

public class SetCommand extends  StoreCommand<SetReponse> {

	public SetReponse create() {
		return new SetReponse();
	}

	final public Name getName() {
		return Name.set;
	}



}
