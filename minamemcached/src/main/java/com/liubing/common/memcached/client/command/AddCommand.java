package com.liubing.common.memcached.client.command;

import com.liubing.common.memcached.reponse.AddReponse;

public class AddCommand extends StoreCommand<AddReponse> {

	public AddReponse create() {
		
		return new AddReponse();
	}

	public Name getName() {
		
		return Name.add;
	}

}
