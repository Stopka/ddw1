
package cz.ctu.fit.ddw.skorpste.movie;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Milan Dojchinovski
 * <milan (at) dojchinovski (dot) mk>
 * Twitter: @m1ci
 * www: http://dojchinovski.mk
 */
public class GateClient {
    
    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;
    
    // whether the GATE is initialised
    private static boolean isGateInitilised = false;
    
    public int run(String text){
        
        if(!isGateInitilised){
            
            // initialise GATE
            initialiseGate();            
        }        

        try {                
            // create an instance of a Document Reset processing resource
            ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");

            // create an instance of a English Tokeniser processing resource
            ProcessingResource tokenizer = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");

            // create an instance of a Sentence Splitter processing resource
            ProcessingResource sentenceSplitter = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
            
            FeatureMap transducerFeatureMap = Factory.newFeatureMap();
            transducerFeatureMap.put("lexiconURL", new URL("file:/home/stopka/.GATE_Developer_7.1/plugins/ANNIE/resources/heptag/lexicon"));
            ProcessingResource postagger = (ProcessingResource) Factory.createResource("gate.creole.POSTagger",transducerFeatureMap);
            
            transducerFeatureMap = Factory.newFeatureMap();
            transducerFeatureMap.put("language", "english");
            ProcessingResource stemmer = (ProcessingResource) Factory.createResource("stemmer.SnowballStemmer",transducerFeatureMap);
            
            transducerFeatureMap = Factory.newFeatureMap();
            transducerFeatureMap.put("encoding", "UTF-8");
            transducerFeatureMap.put("minimumNicknameLikelihood", 0.5);
            transducerFeatureMap.put("definitionFileURL", new URL("file:/home/stopka/.GATE_Developer_7.1/plugins/ANNIE/resources/othomatcher/listsNM.def"));
            ProcessingResource morphologyanalysis = (ProcessingResource) Factory.createResource("gate.creole.morph.Morph");
            
            // locate the JAPE grammar file
            File japeOrigFile = new File("hw.jape");
            java.net.URI japeURI = japeOrigFile.toURI();
            
            // create feature map for the transducer
            transducerFeatureMap = Factory.newFeatureMap();
            transducerFeatureMap.put("grammarURL", japeURI.toURL());
                transducerFeatureMap.put("encoding", "UTF-8");
             
            
            // create an instance of a JAPE Transducer processing resource
            ProcessingResource japeTransducerPR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerFeatureMap);

            // create corpus pipeline
            annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");

            // add the processing resources (modules) to the pipeline
            annotationPipeline.add(documentResetPR);
            annotationPipeline.add(tokenizer);
            annotationPipeline.add(sentenceSplitter);
            annotationPipeline.add(postagger);
            annotationPipeline.add(stemmer);
            annotationPipeline.add(morphologyanalysis);
            annotationPipeline.add(japeTransducerPR);
            
            // create a document
            Document document = Factory.newDocument(text);

            // create a corpus and add the document
            Corpus corpus = Factory.newCorpus("");
            corpus.add(document);

            // set the corpus to the pipeline
            annotationPipeline.setCorpus(corpus);

            //run the pipeline
            annotationPipeline.execute();

            // loop through the documents in the corpus
            for(int i=0; i< corpus.size(); i++){

                Document doc = corpus.get(i);

                // get the default annotation set
                AnnotationSet as_default = doc.getAnnotations();

                FeatureMap futureMap = null;
                // get all Token annotations
                
                AnnotationSet annSetTokens = as_default.get("Marked",futureMap);

                ArrayList tokenAnnotations = new ArrayList(annSetTokens);

                // looop through the Token annotations
                int pos=0;
                int neg=0;
                
                for(int j = 0; j < tokenAnnotations.size(); ++j) {

                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);

                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    //System.out.print("["+underlyingString+"]");
                    
                    // get the features of the token
                    FeatureMap annFM = token.getFeatures();
                    // get the value of the "string" feature
                    /*for(Object value:(Set<Object>)annFM.keySet()){
                        System.out.print(" "+value+"="+annFM.get(value)+";");
                    }*/
                    String stem=(String)annFM.get("string");
                    System.out.print(stem);
                    Scanner scanner = new Scanner(new File("pos.txt"));
                    while (scanner.hasNextLine()) {
                       final String lineFromFile = scanner.nextLine();
                       if(lineFromFile.contains(stem)) { 
                           System.out.println("+");
                           pos++;
                           break;
                       }
                    }
                    scanner = new Scanner(new File("neg.txt"));
                    while (scanner.hasNextLine()) {
                       final String lineFromFile = scanner.nextLine();
                       if(lineFromFile.contains(stem)) { 
                           System.out.println("-");
                           neg++;
                           break;
                       }
                    }
                    System.out.println();
                    //String value = (String)annFM.get((Object)"kind");
                    //System.out.println("Category: " + value);*/
                }
                System.out.println();
                System.out.println("sum:"+(pos+neg)+" 100%");
                double poss=(double)pos/(double)(pos+neg)*100;
                System.out.println("pos:"+pos+" "+poss+"%");
                double negs=(double)neg/(double)(pos+neg)*100;
                System.out.println("neg:"+neg+" "+negs+"%");
                double limit=60;
                if(poss>limit){
                    System.out.println("Positive! (+)");
                    return 1;
                }else
                if(negs>limit){
                    System.out.println("Negative! (-)");
                    return -1;
                }else{
                    System.out.println("Neutral! (*)");
                    return 0;
                }
            }
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException e) {
                System.out.println("Malformed URL");
                System.out.println(e.toString());
        } catch(FileNotFoundException ex){
            System.out.println(ex.toString());
        }
        return 0;
    }

    private void initialiseGate() {
        
        try {
            // set GATE home folder
            // Eg. /Applications/GATE_Developer_7.0
            File gateHomeFile = new File("/home/stopka/.GATE_Developer_7.1");
            Gate.setGateHome(gateHomeFile);
            
            // set GATE plugins folder
            // Eg. /Applications/GATE_Developer_7.0/plugins            
            File pluginsHome = new File("/home/stopka/.GATE_Developer_7.1/plugins");
            Gate.setPluginsHome(pluginsHome);            
            
            // set user config file (optional)
            // Eg. /Applications/GATE_Developer_7.0/user.xml
            Gate.setUserConfigFile(new File("/home/stopka/.GATE_Developer_7.1", "user.xml"));            
            
            // initialise the GATE library
            Gate.init();
            
            // load ANNIE plugin
            CreoleRegister register = Gate.getCreoleRegister();
            URL annieHome = new File(pluginsHome, "ANNIE").toURL();
            register.registerDirectories(annieHome);
            
            annieHome = new File(pluginsHome, "Stemmer_Snowball").toURL();
            register.registerDirectories(annieHome);
            
            annieHome = new File(pluginsHome, "Tools").toURL();
            register.registerDirectories(annieHome);
            
            // flag that GATE was successfuly initialised
            isGateInitilised = true;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(GateClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}