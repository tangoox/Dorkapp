package com.septinary.xbwapp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtils {
	
	private static class JsonUtilsHolder {
		private final static JsonUtils INSTANCE = new JsonUtils();
	}

	public JsonUtils() {

	}

	public static JsonUtils getInstance() {
		return JsonUtilsHolder.INSTANCE;
	}

	// 返回Json对象,对应Key的Value
	public JSONObject getJSONObject(String jstr, String key) {
		try {
			JSONObject jsonObject = new JSONObject(jstr).getJSONObject(key);
			return jsonObject;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// jsonObject 转化成String[]
	public String[] getJSONObjToStringArray(String jstr, String key,
			String keys[]) {
		String[] ss = new String[keys.length];
		try {
			String jsonStringer = new JSONObject(jstr).getString(key);
			for (int i = 0; i < keys.length; i++) {

				String josn = new JSONObject(jsonStringer).getString(keys[i])
						.toString();
				ss[i] = josn;
			}
			return ss;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// jsonObject 转化成list<Hash<String,String>>
	public static List<HashMap<String, String>> getJSONObjToList(String jstr,
			String key, String keys[]) {
		List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> info = new HashMap<String, String>();
		try {
			String jsonStringer = new JSONObject(jstr).getString(key);
			for (int i = 0; i < keys.length; i++) {

				String josn = new JSONObject(jsonStringer).optString(keys[i])
						.toString();
				info.put(keys[i], josn);
			}
			infos.add(info);
			return infos;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getJSONString(String jstr, String key) { // 转换成字符串

		try {
			String jsonStringer = new JSONObject(jstr).getString(key);
			return jsonStringer;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public HashMap<String, String> getJSONString(String jstr, String keys[]) {
		HashMap<String, String> rshash = new HashMap<String, String>();
		try {
			JSONObject jsonobject = new JSONObject(jstr);
			for (int i = 0; i < keys.length; i++) {
				jsonobject.keys();
				String tjson = jsonobject.getString(keys[i]);
				rshash.put(keys[i], tjson);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rshash;
	}

	// 传入Json数组,返回List HashMap<String,String> 数组
	public List<HashMap<String, String>> getJsonArrayList(String jstr,
			String key, String[] keyset) {

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONArray jsonArray = new JSONObject(jstr).getJSONArray(key);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonobj = (JSONObject) jsonArray.opt(i);
				HashMap<String, String> hashmap = new HashMap<String, String>();
				for (int j = 0; j < keyset.length; j++) {

					hashmap.put(keyset[j], jsonobj.optString(keyset[j]));
				}
				list.add(hashmap);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String[] getAreaToList(String rs, String flag) {
		try {

			String jsonAry = new JSONObject(rs).getString("data");
			JSONArray json = new JSONArray(jsonAry);
			String[] areainfo = new String[json.length()];
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				String area = jsonObject.getString(flag);

				areainfo[i] = area;
			}
			return areainfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String[] getQuYuToList(String rs) { // 转换成字符数组
		try {

			String jsonAry = new JSONObject(rs).getString("Datas");
			JSONArray json = new JSONArray(jsonAry);
			String[] areainfo = new String[json.length()];
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				String area = jsonObject.getString("name");

				areainfo[i] = area;
			}
			return areainfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String[] getToStringArray(String rs, String keys) { // 转换成字符数组
		try {

			// String jsonAry = new JSONObject(rs).getString("keys");
			JSONArray json = new JSONArray(rs);
			String[] areainfo = new String[json.length()];
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				String area = jsonObject.getString(keys);

				areainfo[i] = area;
			}
			return areainfo;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 将jsonarray数组转换成List数组
	public List<HashMap<String, String>> getJsonArrToList(String rs,
			String[] keys) { // 转换成字符数组
		try {
			// String jsonAry = new JSONObject(rs).getString("keys");
			JSONArray json = new JSONArray(rs);
			List<HashMap<String, String>> infos = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				for (int j = 0; j < keys.length; j++) {
					String strname = jsonObject.optString(keys[j]);
					map.put(keys[j], strname);
				}
				infos.add(map);
			}
			return infos;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public HashMap<String, String> getJsonToHashMap(String jsonStr){
		HashMap<String, String> list = new HashMap<String,String>();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			Iterator<?> it = jsonObject.keys();
			while(it.hasNext()){
				String key = it.next().toString();
				String value = jsonObject.getString(key).toString();
				list.put(key, value);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	} 
	
	// 返回List<List<HashMap<String, String>>>类型
	public List<List<HashMap<String, String>>> getJsonHashList(String jstr,
			String key, String[] keys, String[] keys1) {
		List<List<HashMap<String, String>>> list = new ArrayList<List<HashMap<String, String>>>();
		try {
			JSONArray jsonArray = new JSONObject(jstr).getJSONArray(key);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray objectList = jsonArray.getJSONArray(i);
				List<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();
				if (i == 0 && keys1 != null) {
					for (int j = 0; j < objectList.length(); j++) {
						JSONObject item = objectList.getJSONObject(j);
						HashMap<String, String> map = new HashMap<String, String>();
						for (int k = 0; k < keys1.length; k++) {
							String strname = item.optString(keys1[k]);
							map.put(keys1[k], strname);
						}
						itemList.add(map);
					}
				} else {
					for (int j = 0; j < objectList.length(); j++) {
						JSONObject item = objectList.getJSONObject(j);
						HashMap<String, String> map = new HashMap<String, String>();
						for (int k = 0; k < keys.length; k++) {
							String strname = item.optString(keys[k]);
							map.put(keys[k], strname);
						}
						itemList.add(map);
					}
				}
				list.add(itemList);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 返回List<List<HashMap<String, String>>>类型
	public List<List<HashMap<String, String>>> getJsonHashList(String jstr,
			String key, String[] keys) {
		List<List<HashMap<String, String>>> list = new ArrayList<List<HashMap<String, String>>>();
		try {
			JSONArray jsonArray = new JSONObject(jstr).getJSONArray(key);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONArray objectList = jsonArray.getJSONArray(i);
				List<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();

				for (int j = 0; j < objectList.length(); j++) {
					JSONObject item = objectList.getJSONObject(j);
					HashMap<String, String> map = new HashMap<String, String>();
					for (int k = 0; k < keys.length; k++) {
						String strname = item.optString(keys[k]);
						map.put(keys[k], strname);
					}
					itemList.add(map);
				}

				list.add(itemList);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
