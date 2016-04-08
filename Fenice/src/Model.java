import java.util.LinkedList;


public class Model {

	/**
	 * @param args
	 */

}

class Risorsa
{
	protected String nome="";
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Risorsa (String nome)
	{
		this.nome=nome;
	}
	@Override
	public String toString() {
		return  "[nome=" + nome + "]";
	}
	
	
	
}

class Lavoratore extends Risorsa
{
	private String qualifica="";
	
	public String getQualifica() {
		return qualifica;
	}
	public void setQualifica(String qualifica) {
		this.qualifica = qualifica;
	}
	public Lavoratore (String nome, String qualifica)
	{
		super (nome);
		this.qualifica=qualifica;
	}
	@Override
	public String toString() {
		String res=super.toString();
		return res+"Lavoratore [qualifica=" + qualifica + "]";
	}
	
}

class Strumento extends Risorsa
{
	public Strumento (String nome)
	{
		super (nome);
	}

	@Override
	public String toString() {
		String res=super.toString();
		return res+"Strumento [nome=" + nome + "]";
	}
	
	
	
}

class Materiale extends Risorsa
{
	private int quanti=0;
	
	public int getQuanti() {
		return quanti;
	}
	public void setQuanti(int quanti) {
		this.quanti = quanti;
	}
	public Materiale (String nome, int quant)
	{
		super (nome);
		this.quanti=quant;
	}
	@Override
	public String toString() {
		String res=super.toString();
		return res+"Materiale [quanti=" + quanti + "]";
	}
	
}

abstract class Attivita
{
	protected String codIdentif="";
	protected String descrizione="";
	
	public String getCodIdentif() {
		return codIdentif;
	}
	public void setCodIdentif(String codIdentif) {
		this.codIdentif = codIdentif;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
	public Attivita (String codice, String descrizione)
	{
		this.codIdentif=codice;
		this.descrizione=descrizione;
	}
	public abstract int calcolaTempo ();
	@Override
	public String toString() {
		return "Attivita [codIdentif=" + codIdentif + ", descrizione="
				+ descrizione + "]";
	}
	
	
}

class Elementare extends Attivita
{
	
	public LinkedList<Risorsa> getStrumento() {
		return strumento;
	}

	public void setStrumento(LinkedList<Risorsa> strumento) {
		this.strumento = strumento;
	}

	public LinkedList<Risorsa> getLavoratori() {
		return lavoratori;
	}

	public void setLavoratori(LinkedList<Risorsa> lavoratori) {
		this.lavoratori = lavoratori;
	}

	public LinkedList<Risorsa> getMateriali() {
		return materiali;
	}

	public void setMateriali(LinkedList<Risorsa> materiali) {
		this.materiali = materiali;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	private LinkedList <Risorsa> strumento=new LinkedList <Risorsa> ();
	private LinkedList <Risorsa> lavoratori=new LinkedList <Risorsa> ();
	private LinkedList <Risorsa> materiali=new LinkedList <Risorsa> ();
	int durata = 0;
	public Elementare (String codiceIdentif, String descrizione, int dur)
	{
		super (codiceIdentif, descrizione);
		this.durata=dur;
	}
	
	public void addLavoratori (Risorsa r)
	{
		lavoratori.add(r);
	}
	
	public void addStrumento (Risorsa r)
	{
		strumento.add(r);
	}
	
	public void addMateriali (Risorsa r)
	{
		materiali.add(r);
	}
	
	public int calcolaTempo ()
	{
		return durata;
	}

	@Override
	public String toString() {
		int dim=strumento.size();
		String res=" Attivita' elementare :"+super.toString()+" Strumenti: [";
		for (int i=0;i<dim;i++)
		{
			res+=strumento.get(i).toString();
		}
		res+="]";
		dim=lavoratori.size();
		res+=" Lavoratori: [";
		for (int i=0;i<dim;i++)
		{
			res+=lavoratori.get(i).toString();
		}
		res+="]";
		dim=materiali.size();
		res+=" Materiali: [";
		for (int i=0;i<dim;i++)
		{
			res+=materiali.get(i).toString();
		}
		res+="]";
		res+="Durata: ";
		res+=Integer.toString(durata);
		return res;
	}
	
	
	
}

class Composte extends Attivita
{
	
	protected LinkedList <Attivita> att=new LinkedList <Attivita>();
	
	public LinkedList<Attivita> getAtt() {
		return att;
	}

	public void setAtt(LinkedList<Attivita> att) {
		this.att = att;
	}

	public Composte (String codiceIdentif, String descrizione)
	{
		super (codiceIdentif, descrizione);
	}
	
	public void add (Attivita a)
	{
		this.att.add(a);
	}
	
	public int calcolaTempo ()
	{
		int tempo=0;
		int dim=att.size();
		
		for (int i=0;i<dim;i++)
		{
			tempo+=att.get(i).calcolaTempo();
		}
		
		return tempo;
	}

	@Override
	public String toString() {
		int dim=att.size();
		String res="Composta: ";
		for (int i=0;i<dim;i++)
		{
			res+=att.get(i).toString();
		}
		return res;
	}
	
	
	
}
