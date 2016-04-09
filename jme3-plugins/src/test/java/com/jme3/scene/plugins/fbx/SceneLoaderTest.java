package com.jme3.scene.plugins.fbx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.file.FbxFile;
import com.jme3.scene.plugins.fbx.file.FbxReader;

import javassist.expr.MethodCall;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;

@PrepareForTest({FbxReader.class, Node.class, SceneLoader.class})
@RunWith(PowerMockRunner.class)
public class SceneLoaderTest {
    @Mock
    public SceneKey sk;
    public ModelKey mk;
    public AssetInfo assetInfo;
    
    private SceneLoader sc;
    
    @Before
    public void setUp() {
        sc = new SceneLoader();
        sk = Mockito.mock(SceneKey.class);
        mk = Mockito.mock(ModelKey.class);
        assetInfo = Mockito.mock(AssetInfo.class);        
    }
    
    @Test(expected=NullPointerException.class)
    public void testSceneLoaderLoadWithNull() throws IOException {
        sc.load(null);
    }
    
    @Test(expected=AssetLoadException.class)
    public void testSceneLoaderWithAssetInfoMock() throws IOException {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);       
        sc.load(assetInfo);
    }
    
    @Test
    public void testSceneLoaderWithModelKeyMock() throws Exception {
        AssetManager assetMgrMock = Mockito.mock(AssetManager.class);
        InputStream inputMock = Mockito.mock(InputStream.class);
        MaterialDef materialDefMock = Mockito.mock(MaterialDef.class);
        MatParam  matParamMock = Mockito.mock(MatParam.class);
        
        Node n = new Node();
        String childNodeName = "meshChildGetsMaterial";
        n.setName(childNodeName);
        Node meshNodeSpied = Mockito.spy(n);
        Node sceneNode = new Node();
        String sceneNodeName = "Fr-scene";
        sceneNode.setName(sceneNodeName);
        Node rootMeshNode = new Node();
        String rootMeshNodeName = "mesh";
        rootMeshNode.setName(rootMeshNodeName);
        Node rootMeshNodeSpied = Mockito.spy(rootMeshNode);
        
        
        FbxFile fbxFileMock = new FbxFile();
        FbxElement fbxElementMock = new FbxElement(2);
        
        fbxElementMock.id = "Objects";
        
        FbxElement fbxMaterialMock = new FbxElement(2);
        fbxMaterialMock.id = "Material";
        fbxMaterialMock.properties.add(100L);
        fbxMaterialMock.properties.add("abc\0def");
        fbxMaterialMock.properties.add("");
        
        FbxElement fbxMeshMock = new FbxElement(2); 
        fbxMeshMock.id = "Model";
        fbxMeshMock.properties.add(200L);
        fbxMeshMock.properties.add("mesh\0ghi");
        fbxMeshMock.properties.add("P");
        
        FbxElement fbxMeshChildMock = new FbxElement(2); 
        fbxMeshChildMock.id = "Model";
        fbxMeshChildMock.properties.add(300L);
        fbxMeshChildMock.properties.add("meshChildGetsMaterial\0ghi");
        fbxMeshChildMock.properties.add("P");
        
        
        FbxElement fbxConnectionMock = new FbxElement(2);
        fbxConnectionMock.id = "Connections";
        
        FbxElement fbxConcreteLinkMock = new FbxElement(3);
        fbxConcreteLinkMock.id = "C";
        fbxConcreteLinkMock.properties.add("OO");
        fbxConcreteLinkMock.properties.add(100L);   //material id
        fbxConcreteLinkMock.properties.add(200L);   //mesh id
        FbxElement fbxRootNodeLinkMock = new FbxElement(3);
        fbxRootNodeLinkMock.id = "C";
        fbxRootNodeLinkMock.properties.add("OO");
        fbxRootNodeLinkMock.properties.add(200L);   //mesh id
        fbxRootNodeLinkMock.properties.add(0L);   //rootNode id (default)
        
        FbxElement fbxMeshNodeLinkMock = new FbxElement(3);
        fbxMeshNodeLinkMock.id = "C";
        fbxMeshNodeLinkMock.properties.add("OO");
        fbxMeshNodeLinkMock.properties.add(300L);   //mesh child id
        fbxMeshNodeLinkMock.properties.add(200L);   //mesh id (default)
        
        fbxConnectionMock.children.add(fbxConcreteLinkMock);
        fbxConnectionMock.children.add(fbxRootNodeLinkMock);
        fbxConnectionMock.children.add(fbxMeshNodeLinkMock);
        
        Mockito.when(assetInfo.getManager()).thenReturn(assetMgrMock);     
        Mockito.when(assetInfo.openStream()).thenReturn(inputMock);
        Mockito.when(assetInfo.getKey()).thenReturn(mk);
        
        Mockito.when(assetMgrMock.loadAsset(Mockito.any(AssetKey.class))).thenReturn(materialDefMock);
        
        Mockito.when(mk.getName()).thenReturn("Fromage");
        Mockito.when(mk.getExtension()).thenReturn("chee");
        
        Mockito.when(materialDefMock.getMaterialParam(Mockito.anyString())).thenReturn(matParamMock);

        fbxFileMock.rootElements.add(fbxConnectionMock);
        fbxFileMock.rootElements.add(fbxElementMock);
        fbxElementMock.children.add(fbxMaterialMock);
        fbxElementMock.children.add(fbxMeshMock);
        fbxElementMock.children.add(fbxMeshChildMock);
           
        PowerMockito.mockStatic(FbxReader.class);       
        PowerMockito.when(FbxReader.readFBX(Mockito.any(InputStream.class))).thenReturn(fbxFileMock);
        
        //when meshChildGetsMaterial gets instantiated, put the spied object there instead:
        // same holds for rootMeshNode
        PowerMockito.whenNew(Node.class).withArguments(childNodeName).thenReturn(meshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(rootMeshNodeName).thenReturn(rootMeshNodeSpied);
        PowerMockito.whenNew(Node.class).withArguments(sceneNodeName).thenReturn(sceneNode);
        
        Node scene = (Node) sc.load(assetInfo);
        Spatial mesh = scene.getChild("mesh");
        Spatial meshWithMaterial = ((Node) mesh).getChild("meshChildGetsMaterial");
        assertNotEquals(null, scene.getChild("mesh"));
        assertNotEquals(null, meshWithMaterial);
        
        Mockito.verify(meshNodeSpied, Mockito.times(1)).setMaterial(Mockito.any(Material.class));
        Mockito.verify(rootMeshNodeSpied, Mockito.never()).setMaterial(Mockito.any(Material.class));
    }
}
