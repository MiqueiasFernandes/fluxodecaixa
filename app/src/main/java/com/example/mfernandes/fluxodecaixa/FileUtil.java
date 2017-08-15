package com.example.mfernandes.fluxodecaixa;

/**
 * Created by mfernandes on 10/08/17.
 */




    import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
    import java.io.FileWriter;
    import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Iterator;

    import android.app.Activity;
    import android.content.SharedPreferences;
    import android.os.Environment;
    import android.widget.Toast;

public class FileUtil {


        public static final String PREFS_NAME = "FluxoDeCaixa";

static int minID=-1, maxID=-1;
        public static final ArrayList<Lancamento> lancamentos=  new ArrayList<>();


        static String getConfiguracao(Activity act, String nome){
            // Restore preferences
            SharedPreferences settings = act.getSharedPreferences(PREFS_NAME, 0);
            return settings.getString(nome, null);
        }

        static boolean setConfiguracao(Activity act, String nome, String valor){
            // We need an Editor object to make preference changes.
            // All objects are from android.context.Context
            SharedPreferences settings = act.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(nome, valor);

            // Commit the edits!
           return editor.commit();
        }







        private static BufferedReader bufferedReader;
        private static BufferedWriter bufferedWriter;

        private static boolean checkSdCard() throws Exception {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // SD montado, podemos ler e escrever no disco
                return true;
            } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                throw new Exception("SDCARD configurado para somente leitura.");
            }
            throw new Exception("Não é possível gravar no SDCARD.");
        }

        /***********************************************************************
         *                      MÉTODOS DE LEITURA                             *
         **********************************************************************/
        public static boolean createBufferedReader(String nomeArquivo)
                throws IOException, Exception {
            if (checkSdCard()) {
                File file = new File(Environment.getExternalStorageDirectory()
                        + "/fluxoDeCaixa/" + nomeArquivo + ".txt");
                if (file.exists()) {
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "ISO-8859-1"));
                    return true;
                } else {
                    throw new Exception("Arquivo não encontrado.");
                }
            }
            return false;
        }

        public static String getText() throws IOException {
            String aux = null;
            String ret = "";
            while ((aux = bufferedReader.readLine()) != null) {
                ret += aux + "\n";
            }

            return ret;
        }

        public static String readLine() throws IOException{
            return bufferedReader.readLine();
        }

        public static boolean closeReader() throws IOException {
            bufferedReader.close();
            return true;
        }

        /***********************************************************************
         *                      MÉTODOS DE ESCRITA                             *
         **********************************************************************/
        public static boolean createBufferedWriter(String nomeArquivo, boolean append)
                throws IOException, Exception {
            if (checkSdCard()) {


                File file = new File(Environment.getExternalStorageDirectory()
                        + "/fluxoDeCaixa/");
                if(!file.exists())
                    file.mkdirs();


                 file = new File(Environment.getExternalStorageDirectory()
                        + "/fluxoDeCaixa/" + nomeArquivo + ".txt");
                if(!file.exists())
                file.createNewFile();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, append), "ISO-8859-1"));

                return true;
            }
            return false;
        }

        public static boolean write(String text) throws IOException {
            bufferedWriter.write(text);
            return true;
        }

        public static boolean appendLine(String line) throws IOException{
            bufferedWriter.append(line+"\n");
            return true;
        }

        public static boolean closeWriter() throws IOException {
            bufferedWriter.close();
            return true;
        }


        public static void writeLancamento(Lancamento lancamento, Activity a) throws Exception {
            createBufferedWriter("caixa-" + FileUtil.getConfiguracao(a, "referencia"), true);
            appendLine(lancamento.toString());
            closeWriter();
        }



        public static String getNomeByNum(int num) {
            String m = "mes";
            switch (num){
                case 0:
                    m = "janeiro";
                    break;
                case 1:
                    m = "fevereiro";
                    break;
                case 2:
                    m = "marco";
                    break;
                case 3:
                    m = "abril";
                    break;
                case 4:
                    m = "maio";
                    break;
                case 5:
                    m = "junho";
                    break;
                case 6:
                    m = "julho";
                    break;
                case 7:
                    m = "agosto";
                    break;
                case 8:
                    m = "setembro";
                    break;
                case 9:
                    m = "outubro";
                    break;
                case 10:
                    m = "novembro";
                    break;
                case 11:
                    m = "dezembro";
                    break;
            }

            return m;
        }


        public static int getNumByNome(String nome) {
            switch (nome){
                case "janeiro":
                    return 0;
                case "fevereiro":
                    return 1;
                case "marco":
                    return 2;
                case "abril":
                    return 3;
                case "maio":
                    return 4;
                case "junho":
                    return 5;
                case "julho":
                    return 6;
                case "agosto":
                    return 7;
                case "setembro":
                    return 8;
                case "outubro":
                    return 9;
                case "novembro":
                    return 10;
                case "dezembro":
                    return 11;
            }
            return -1;
        }




    public static int getMesAtual() {
        return  Calendar.getInstance().get(Calendar.MONTH);
    }
    public static int getDiaAtual() {
        return  Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

        public static void addLancamento(Lancamento lancamento){
            lancamentos.add(lancamento);
        }

        public static void clearLancamento(){
            lancamentos.clear();
        }

        public static Iterator<Lancamento> getLancamentos(){
           return lancamentos.iterator();
        }


        public static void removeLancamento(int id, Activity a) throws Exception {
            Lancamento remover = null;
            for(Lancamento lancamento : lancamentos) {
                if (lancamento.getId() == id) {
                    remover = lancamento;
                }
            }
            if (remover != null) {
                lancamentos.remove(remover);
               File file = new File(Environment.getExternalStorageDirectory()
                        + "/fluxoDeCaixa/caixa-" + FileUtil.getConfiguracao(a, "referencia") + ".txt");
                if(file.exists())
                    file.delete();
                for(Lancamento lancamento : lancamentos) {
                    writeLancamento(lancamento, a);
                }
                Toast.makeText(a,
                        "O lançamento ID " + id + " foi excluido com sucesso!",
                        Toast.LENGTH_LONG).show();
            }

        }



      public static int[]  popular(Activity a) throws Exception {
            lancamentos.clear();
          int cont = 0;
          int erros = 0;
          String line = "";
         createBufferedReader("caixa-" + FileUtil.getConfiguracao(a, "referencia"));
          while((line = FileUtil.readLine()) != null){
try{
    cont++;
    Lancamento l = new Lancamento(line);
    if (cont <= 1) {
        minID =  maxID = l.getId();
    } else {
        minID = Math.min(l.getId(), minID);
        maxID = Math.max(l.getId(), maxID);
    }
    addLancamento(l);
}catch (Exception e){
    Toast.makeText(a,
            "A linha " + cont + " não pode ser importada.",
            Toast.LENGTH_LONG).show();
}
          }

          closeReader();
          return new int[]{cont, erros};
        }


       public static int[] getRangeId(Activity  a) throws Exception {
           if(lancamentos.size() < 1){
               popular(a);
           }
           if(lancamentos.size() > 0){
               return new int[] {minID, maxID};
           }
           throw new Exception("Não há lançamentos neste mês.");
        }



    }


