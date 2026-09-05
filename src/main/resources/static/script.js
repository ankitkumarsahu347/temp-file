const form = document.getElementById('todoForm');
const input = document.getElementById('todoInput');
const list = document.getElementById('todoList');
const count = document.getElementById('taskCount');
const emptyState = document.getElementById('emptyState');
const clearCompleted = document.getElementById('clearCompleted');

async function api(url, options = {}) {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  });
  if (!response.ok) throw new Error(`Request failed: ${response.status}`);
  return response.status === 204 ? null : response.json();
}

async function loadTasks() {
  try {
    const todos = await api('/api/tasks');
    render(todos);
  } catch (error) {
    list.innerHTML = '<li class="error">Could not load tasks. Check that the server and MySQL are running.</li>';
  }
}

function render(todos) {
  list.innerHTML = '';
  todos.forEach(todo => {
    const item = document.createElement('li');
    item.className = `todo-item ${todo.completed ? 'completed' : ''}`;

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.checked = todo.completed;
    checkbox.addEventListener('change', async () => {
      await api(`/api/tasks/${todo.id}`, {
        method: 'PUT',
        body: JSON.stringify({ title: todo.title, completed: checkbox.checked })
      });
      loadTasks();
    });

    const text = document.createElement('span');
    text.textContent = todo.title;

    const deleteButton = document.createElement('button');
    deleteButton.className = 'delete';
    deleteButton.textContent = 'Delete';
    deleteButton.addEventListener('click', async () => {
      await api(`/api/tasks/${todo.id}`, { method: 'DELETE' });
      loadTasks();
    });

    item.append(checkbox, text, deleteButton);
    list.appendChild(item);
  });

  const remaining = todos.filter(todo => !todo.completed).length;
  count.textContent = `${remaining} ${remaining === 1 ? 'task' : 'tasks'} remaining`;
  emptyState.style.display = todos.length ? 'none' : 'block';
}

form.addEventListener('submit', async event => {
  event.preventDefault();
  const title = input.value.trim();
  if (!title) return;

  try {
    await api('/api/tasks', {
      method: 'POST',
      body: JSON.stringify({ title })
    });
    input.value = '';
    input.focus();
    loadTasks();
  } catch (error) {
    alert('Could not add the task.');
  }
});

clearCompleted.addEventListener('click', async () => {
  await api('/api/tasks/completed', { method: 'DELETE' });
  loadTasks();
});

loadTasks();
