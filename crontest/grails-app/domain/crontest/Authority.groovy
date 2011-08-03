package crontest

class Authority {

	String authority
	String name //add 

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true
		name nullable:true, blank:true //add
	}
}
