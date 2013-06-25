package com.imyquan.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;



public class Person implements Comparable<Person>{
	

	private String name;
	private String avatar_url;
	private boolean isCheck;
	

    public Person() {
        super();
    }
	
   public Person(JSONObject json) throws JSONException{
        
        this.name = json.getString("UName");
        this.avatar_url = json.getString("UAvatar");
        this.isCheck = false;
    }

	public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((avatar_url == null) ? 0 : avatar_url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (avatar_url == null) {
			if (other.avatar_url != null)
				return false;
		} else if (!avatar_url.equals(other.avatar_url))
			return false;
		return true;
	}
	
	

	public static List<Person> constructStatuses(String res) throws JSONException {
			JSONArray list = new JSONArray(res);
			int size = list.length();
			List<Person> personsList = new ArrayList<Person>(size);
			HashSet<Person> personSet = new HashSet<Person>();
			for (int i = 0; i < size; i++) {
				personSet.add(new Person(list.getJSONObject(i)));
			}
			Iterator<Person> iterator = personSet.iterator();
			
			
			while(iterator.hasNext()){
				Person person = iterator.next();
				personsList.add(person);
			}
			
			Collections.sort(personsList);
			return personsList;
	}

	@Override
	public int compareTo(Person another) {
		// TODO Auto-generated method stub
		return this.getName().compareTo(another.getName());
	}
}
