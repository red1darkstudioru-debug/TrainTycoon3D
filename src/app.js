import * as THREE from 'https://unpkg.com/three@0.165.0/build/three.module.js';
import { GLTFLoader } from 'https://unpkg.com/three@0.165.0/examples/jsm/loaders/GLTFLoader.js';

const upgrades = [
  { id: 'platform', title: 'Платформа', description: 'Больше пассажиров садятся на поезд.', baseCost: 80, level: 0, maxLevel: 6, income: 9, passengers: 14, cargo: 2 },
  { id: 'cargo', title: 'Грузовой склад', description: 'Контейнеры дают стабильную прибыль.', baseCost: 150, level: 0, maxLevel: 5, income: 18, passengers: 6, cargo: 6 },
  { id: 'engine', title: 'Локомотив', description: 'Скорость рейсов и базовая выручка растут.', baseCost: 260, level: 0, maxLevel: 5, income: 35, passengers: 0, cargo: 0 },
  { id: 'city', title: 'Город у станции', description: 'Дома и офисы создают новый спрос.', baseCost: 420, level: 0, maxLevel: 4, income: 60, passengers: 32, cargo: 0 },
];

const state = { cash: 320, day: 1, lastEarned: 0 };
const formatMoney = (value) => `$${Math.floor(value).toLocaleString('ru-RU')}`;
const costFor = (u) => Math.floor(u.baseCost * Math.pow(1.72, u.level));
const stats = () => upgrades.reduce((acc, u) => {
  acc.income += u.level * u.income;
  acc.passengers += u.level * u.passengers;
  acc.cargo += u.level * u.cargo;
  return acc;
}, { income: 22, passengers: 24, cargo: 8 });

const root = document.getElementById('root');
root.innerHTML = `
  <main class="app">
    <section class="hero">
      <div>
        <p class="eyebrow">Train Tycoon 3D</p>
        <h1>Построй железнодорожную империю</h1>
        <p class="lead">Покупай станции, склады, локомотивы и районы города. Каждый игровой день приносит прибыль, а 3D-мир растёт вместе с апгрейдами.</p>
      </div>
      <div class="bank"><span>Баланс</span><strong id="cash"></strong><small id="turn"></small></div>
    </section>
    <section class="dashboard">
      <div class="scene" id="scene" aria-label="3D сцена железнодорожного тайкуна"></div>
      <aside class="panel">
        <div class="stats" id="stats"></div>
        <h2>Развитие тайкуна</h2>
        <div id="upgrades"></div>
      </aside>
    </section>
  </main>`;

function renderUi() {
  const s = stats();
  document.getElementById('cash').textContent = formatMoney(state.cash);
  document.getElementById('turn').textContent = `День ${state.day} · +${formatMoney(state.lastEarned || s.income)} / рейс`;
  document.getElementById('stats').innerHTML = `<b>${s.passengers}</b><span>пассажиров</span><b>${s.cargo}т</b><span>груза</span><b>${formatMoney(s.income)}</b><span>база</span>`;
  document.getElementById('upgrades').innerHTML = upgrades.map((u) => {
    const cost = costFor(u);
    const disabled = u.level >= u.maxLevel || state.cash < cost ? 'disabled' : '';
    const label = u.level >= u.maxLevel ? 'Макс' : formatMoney(cost);
    return `<article class="upgrade"><div><h3>${u.title}</h3><p>${u.description}</p><small>Уровень ${u.level}/${u.maxLevel}</small></div><button data-buy="${u.id}" ${disabled}>${label}</button></article>`;
  }).join('');
}

document.addEventListener('click', (event) => {
  const button = event.target.closest('[data-buy]');
  if (!button) return;
  const item = upgrades.find((u) => u.id === button.dataset.buy);
  if (!item || item.level >= item.maxLevel || state.cash < costFor(item)) return;
  state.cash -= costFor(item);
  item.level += 1;
  renderUi();
});

setInterval(() => {
  const s = stats();
  state.lastEarned = s.income + Math.floor(s.passengers * 0.8) + Math.floor(s.cargo * 1.4);
  state.cash += state.lastEarned;
  state.day += 1;
  renderUi();
}, 2400);

function makeTrain(scene) {
  const group = new THREE.Group();
  const red = new THREE.MeshStandardMaterial({ color: '#d63535', roughness: 0.45 });
  const yellow = new THREE.MeshStandardMaterial({ color: '#f8c347', roughness: 0.4 });
  const dark = new THREE.MeshStandardMaterial({ color: '#202838', roughness: 0.7 });
  const body = new THREE.Mesh(new THREE.BoxGeometry(2.2, 0.65, 0.85), red);
  body.position.y = 0.72;
  const cabin = new THREE.Mesh(new THREE.BoxGeometry(0.72, 0.85, 0.82), yellow);
  cabin.position.set(-0.55, 1.08, 0);
  const chimney = new THREE.Mesh(new THREE.CylinderGeometry(0.14, 0.18, 0.55, 16), dark);
  chimney.position.set(0.76, 1.2, 0);
  group.add(body, cabin, chimney);
  for (let i = -0.78; i <= 0.9; i += 0.56) {
    const wheel = new THREE.Mesh(new THREE.CylinderGeometry(0.18, 0.18, 0.12, 20), dark);
    wheel.rotation.z = Math.PI / 2;
    wheel.position.set(i, 0.38, 0.48);
    const wheel2 = wheel.clone();
    wheel2.position.z = -0.48;
    group.add(wheel, wheel2);
  }
  scene.add(group);
  return group;
}

function loadGLTF(url) {
  return new Promise((resolve, reject) => {
    const loader = new GLTFLoader();
    loader.load(url, (g) => resolve(g), undefined, (e) => reject(e));
  });
}

function buildScene() {
  const container = document.getElementById('scene');
  const scene = new THREE.Scene();
  scene.background = new THREE.Color('#8fd3ff');
  const camera = new THREE.PerspectiveCamera(55, container.clientWidth / container.clientHeight, 0.1, 100);
  camera.position.set(5.2, 5.0, 6.0);
  camera.lookAt(0, 0, 0);
  const renderer = new THREE.WebGLRenderer({ antialias: true });
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
  renderer.setSize(container.clientWidth, container.clientHeight);
  renderer.shadowMap.enabled = true;
  container.appendChild(renderer.domElement);

  const sun = new THREE.DirectionalLight('#fff2cf', 3.4);
  sun.position.set(4, 8, 5);
  scene.add(new THREE.HemisphereLight('#ccecff', '#345d36', 1.4), sun);
  const ground = new THREE.Mesh(new THREE.BoxGeometry(11, 0.18, 8), new THREE.MeshStandardMaterial({ color: '#5fba5c' }));
  ground.position.y = -0.1;
  scene.add(ground);

  const railMat = new THREE.MeshStandardMaterial({ color: '#5b3b25', roughness: 0.8 });
  const steel = new THREE.MeshStandardMaterial({ color: '#6f7885', metalness: 0.45, roughness: 0.35 });
  [-0.42, 0.42].forEach((z) => {
    const rail = new THREE.Mesh(new THREE.BoxGeometry(10, 0.08, 0.08), steel);
    rail.position.set(0, 0.16, z);
    scene.add(rail);
  });
  for (let x = -4.6; x <= 4.8; x += 0.55) {
    const tie = new THREE.Mesh(new THREE.BoxGeometry(0.12, 0.08, 1.2), railMat);
    tie.position.set(x, 0.1, 0);
    scene.add(tie);
  }

  const station = new THREE.Group();
  const platform = new THREE.Mesh(new THREE.BoxGeometry(3.2, 0.34, 1.4), new THREE.MeshStandardMaterial({ color: '#d6c2a1' }));
  platform.position.set(-1.2, 0.22, -1.25);
  const roof = new THREE.Mesh(new THREE.ConeGeometry(1.9, 0.75, 4), new THREE.MeshStandardMaterial({ color: '#3a6ea5' }));
  roof.rotation.y = Math.PI / 4;
  roof.scale.z = 0.5;
  roof.position.set(-1.2, 1.38, -1.25);
  station.add(platform, roof);
  scene.add(station);
  const cargoGroup = new THREE.Group();
  const cityGroup = new THREE.Group();
  scene.add(cargoGroup, cityGroup);

  // Placeholder train while loading model
  let train = new THREE.Group();
  train.position.set(0, 0, 0);
  scene.add(train);

  // Try loading external model from /assets/train.glb. If it fails, fall back to procedural train.
  const modelUrl = '/assets/train.glb';
  loadGLTF(modelUrl).then((gltf) => {
    try {
      const model = gltf.scene || gltf.scenes?.[0] || gltf;
      // Adjust model scale/position if necessary
      model.position.set(0, 0, 0);
      model.scale.setScalar(1);
      scene.remove(train);
      train = model;
      scene.add(train);
      console.info('Loaded GLTF model from', modelUrl);
    } catch (e) {
      console.warn('GLTF loaded but could not add to scene, using fallback train:', e);
      scene.remove(train);
      train = makeTrain(scene);
    }
  }).catch((err) => {
    console.info('No external model found at', modelUrl, '- using procedural train. Error:', err?.message || err);
    scene.remove(train);
    train = makeTrain(scene);
  });

  window.addEventListener('resize', () => {
    camera.aspect = container.clientWidth / container.clientHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(container.clientWidth, container.clientHeight);
  });

  function animate() {
    requestAnimationFrame(animate);
    const t = performance.now() * 0.001;
    if (train) {
      // keep animation generic: move group if present
      train.position.x = Math.sin(t * (0.35 + upgrades[2].level * 0.05)) * 3.2;
      // if model faces away, flip based on position
      train.rotation.y = train.position.x > 3.15 ? Math.PI : 0;
    }
    station.scale.setScalar(1 + upgrades[0].level * 0.08);
    while (cargoGroup.children.length < upgrades[1].level * 3) {
      const c = new THREE.Mesh(new THREE.BoxGeometry(0.45, 0.35, 0.45), new THREE.MeshStandardMaterial({ color: ['#e25b4b', '#2d9cdb', '#f2c94c'][cargoGroup.children.length % 3] }));
      c.position.set(1.6 + (cargoGroup.children.length % 4) * 0.55, 0.25 + Math.floor(cargoGroup.children.length / 4) * 0.36, -2.2);
      cargoGroup.add(c);
    }
    while (cityGroup.children.length < upgrades[3].level * 4) {
      const h = 0.7 + (cityGroup.children.length % 5) * 0.22;
      const b = new THREE.Mesh(new THREE.BoxGeometry(0.52, h, 0.52), new THREE.MeshStandardMaterial({ color: ['#f2994a', '#56ccf2', '#bb6bd9', '#27ae60'][cityGroup.children.length % 4] }));
      b.position.set(-4 + (cityGroup.children.length % 6) * 0.75, h / 2, 2.1 + Math.floor(cityGroup.children.length / 6) * 0.75);
      cityGroup.add(b);
    }
    renderer.render(scene, camera);
  }
  animate();
}

renderUi();
buildScene();
