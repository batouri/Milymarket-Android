package com.hitasoft.apps.adapters;

import java.util.ArrayList;

import com.hitasoft.apps.milymarket.R;
import com.hitasoft.apps.milymarket.util.GetSet;

public class Model {

	public static ArrayList<Item> Items;

	public static void LoadModel() {

		Items = new ArrayList<Item>();
		if (GetSet.isLogged()) {
			Items.add(new Item(1, R.drawable.setti, R.string.settings));
			Items.add(new Item(2, R.drawable.favo, R.string.MostPopular));
			Items.add(new Item(3, R.drawable.invit, R.string.InviteFriends));
			Items.add(new Item(4, R.drawable.m_add, R.string.addproduct));
			Items.add(new Item(5, R.drawable.logout, R.string.SignOut));

		} else {
			Items.add(new Item(1, R.drawable.login, R.string.SignIn));

		}
	}

	public static Item GetbyId(int id) {

		for (Item item : Items) {
			if (item.Id == id) {
				return item;
			}
		}

		return null;
	}

}
